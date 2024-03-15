#!/usr/bin/env bash

APP_NAME=doomz

cd /home/ubuntu/app

echo "> Check the currently running container"
CONTAINER_ID=$(docker ps -aqf "name=$APP_NAME")

if [ -z "$CONTAINER_ID" ];
then
  echo "> No such container is running."
else
  echo "> Stop and remove container: $CONTAINER_ID"
  docker stop "$CONTAINER_ID"
  docker rm "$CONTAINER_ID"
fi

echo "> Stopping all services using Docker Compose"
docker-compose down

echo "> Removing previous Docker image: $APP_NAME"
docker rmi -f "$APP_NAME"

echo "> Building doomz service using Docker Compose"
docker-compose -f docker-compose.yml build "$APP_NAME" "$APP_NAME"

echo "> Starting all services using Docker Compose"
docker-compose up -d
