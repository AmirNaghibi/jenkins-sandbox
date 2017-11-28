FROM openjdk:8-jdk-alpine
RUN  apk update && apk upgrade && apk add netcat-openbsd
RUN mkdir -p /usr/local/mcr2
ADD target/mcr*.jar /usr/local/mcr2/mcr2.jar
ADD src/main/docker/run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh