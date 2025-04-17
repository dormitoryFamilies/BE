#!/usr/bin/env bash

cd /home/ubuntu/app

EXIST_BLUE=$(docker-compose ps | grep "spring-blue" | grep Up)

if [ -z "$EXIST_BLUE" ]; then
    BEFORE_COLOR="green"
    AFTER_COLOR="blue"
    BEFORE_PORT=8081
    AFTER_PORT=8080
else
    BEFORE_COLOR="blue"
    AFTER_COLOR="green"
    BEFORE_PORT=8080
    AFTER_PORT=8081
fi

# 필수 서비스가 실행중인지 확인
echo "===== 필수 서비스(redis, fluent-bit, prometheus) 상태 확인 ====="
REDIS_RUNNING=$(docker-compose ps | grep "redis" | grep Up)
FLUENT_RUNNING=$(docker-compose ps | grep "fluent-bit" | grep Up)
PROMETHEUS_RUNNING=$(docker-compose ps | grep "prometheus" | grep Up)

# 필수 서비스가 실행중이 아니면 시작
if [ -z "$REDIS_RUNNING" ] || [ -z "$FLUENT_RUNNING" ] || [ -z "$PROMETHEUS_RUNNING" ]; then
    echo "===== 일부 필수 서비스가 실행중이 아닙니다. 서비스 시작 중... ====="
    docker-compose up -d redis fluent-bit prometheus

    # fluent-bit가 완전히 준비될 때까지 대기
    echo "===== fluent-bit 서비스 준비 대기 중... ====="
    for i in {1..10}
    do
        HEALTH=$(docker inspect --format='{{.State.Health.Status}}' fluent-bit 2>/dev/null || echo "starting")
        if [ "$HEALTH" == "healthy" ]; then
            echo "===== fluent-bit 서비스 준비 완료 ====="
            break
        else
            echo "===== fluent-bit 준비 대기 중 ($i/10) ====="
            sleep 5
        fi
    done

    if [ "$HEALTH" != "healthy" ]; then
        echo "===== fluent-bit 서비스가 정상적으로 시작되지 않았습니다. 배포를 중단합니다. ====="
        exit 1
    fi
fi

echo "===== 기존 컨테이너 유지하며 새 컨테이너 배포 시작 ====="
docker-compose build --no-cache spring-${AFTER_COLOR}
docker-compose up -d spring-${AFTER_COLOR}

# 서버 응답 체크 (최대 10번 재시도)
UP="DOWN"
for cnt in {1..10}
do
    echo "===== ${AFTER_COLOR} 서버 응답 확인중(${cnt}/10) ====="
    UP=$(curl -s http://localhost:${AFTER_PORT}/api/actuator/health | jq -r .status)
    if [ "$UP" == "UP" ]; then
        echo "===== ${AFTER_COLOR} 서버 정상 실행됨 ====="
        break
    else
        sleep 10
    fi
done

if [ "$UP" != "UP" ]; then
    echo "===== ${AFTER_COLOR} 서버 실행 실패. 롤백 수행 중... ====="
    docker-compose stop spring-${AFTER_COLOR}
    docker-compose rm -f spring-${AFTER_COLOR}

    echo "===== 배포 실패: 기존(${BEFORE_COLOR}) 컨테이너 유지 ====="
    exit 1
fi

echo "===== Nginx 트래픽 변경: ${BEFORE_PORT} -> ${AFTER_PORT} ====="
sudo sed -i "s/${BEFORE_PORT}/${AFTER_PORT}/" /etc/nginx/nginx.conf
sudo nginx -s reload

echo "===== 기존 컨테이너 종료 및 정리: ${BEFORE_COLOR} (포트 ${BEFORE_PORT}) ====="
docker-compose stop spring-${BEFORE_COLOR}
docker-compose rm -f spring-${BEFORE_COLOR}
docker rmi $(docker images -q app_spring-${BEFORE_COLOR}) || true

echo "===== 로그 파일 백업 ====="
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
if [ -d "./logs/${BEFORE_COLOR}" ]; then
    mkdir -p ./logs/archive
    tar -czf "./logs/archive/${BEFORE_COLOR}_${TIMESTAMP}.tar.gz" "./logs/${BEFORE_COLOR}"
    rm -rf "./logs/${BEFORE_COLOR}"/*
    echo "===== ${BEFORE_COLOR} 서버 로그 백업 완료 ====="
fi

echo "===== 배포 완료. 현재 활성화된 서버: ${AFTER_COLOR} (포트 ${AFTER_PORT}) ====="

# 배포 후 Prometheus 메트릭 확인
echo "===== Prometheus 메트릭 확인 중... ====="
if curl -s "http://localhost:9090/api/v1/query?query=up" | grep -q "spring.${AFTER_COLOR}"; then
    echo "===== Prometheus에서 ${AFTER_COLOR} 서버 메트릭 확인됨 ====="
else
    echo "===== 주의: Prometheus에서 ${AFTER_COLOR} 서버 메트릭이 확인되지 않습니다 ====="
    echo "===== Prometheus 설정을 확인하세요 ====="
fi