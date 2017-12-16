#!/usr/bin/env bash
cd ../frontend
mvn clean package -DskipTests
docker build -f src/main/docker/Dockerfile -t nowheresly/frontend .

cd ../mcr1
mvn clean package -DskipTests
docker build -f src/main/docker/Dockerfile -t nowheresly/mcr1 .

cd ../mcr2
mvn clean package -DskipTests
docker build -f src/main/docker/Dockerfile -t nowheresly/mcr2 .

cd ../mcr3
mvn clean package -DskipTests
docker build -f src/main/docker/Dockerfile -t nowheresly/mcr3 .
