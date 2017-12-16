FROM openjdk:8-jdk-alpine
RUN  apk update && apk upgrade && apk add netcat-openbsd
RUN mkdir -p /usr/local/frontend
ADD target/front*.jar /usr/local/frontend/frontend.jar
ADD src/main/docker/run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh