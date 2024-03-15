#!/usr/bin/env bash

APP_NAME=doomz

cd /home/ubuntu/app

echo "> Stopping all services using Docker Compose"
docker-compose down

echo "> Removing previous Docker image: $APP_NAME"
docker rmi -f "$APP_NAME"

echo "> Building doomz service using Docker Compose"
docker-compose -f docker-compose.yml build "$APP_NAME"

echo "> Starting all services using Docker Compose"
docker-compose up -d