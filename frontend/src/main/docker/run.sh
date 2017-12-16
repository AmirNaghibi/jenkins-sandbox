#!/bin/sh
echo "********************************************************"
echo "Starting FRONTEND"
echo "********************************************************"
exec java -Djava.security.egd=file:/dev/./urandom -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -jar /usr/local/frontend/frontend.jar