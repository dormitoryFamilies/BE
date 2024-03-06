#!/usr/bin/env bash

APP_NAME=doomz

cd /home/ubuntu/app

echo "> Stop and remove existing containers"
docker-compose down

echo "> Remove previous Docker image"
docker rmi -f "$APP_NAME"

echo "> Build and run Docker containers using Docker Compose"
docker-compose up -d