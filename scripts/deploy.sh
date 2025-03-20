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

echo "===== 기존 컨테이너 유지하며 새 컨테이너 배포 시작 ====="
docker-compose build --no-cache spring-${AFTER_COLOR}
docker-compose up -d spring-${AFTER_COLOR}

# 서버 응답 체크 (최대 10번 재시도)
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

echo "===== 배포 완료. 현재 활성화된 서버: ${AFTER_COLOR} (포트 ${AFTER_PORT}) ====="
