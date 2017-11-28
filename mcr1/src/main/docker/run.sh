#!/bin/sh
echo "********************************************************"
echo "Starting MCR1"
echo "********************************************************"
exec java -Djava.security.egd=file:/dev/./urandom -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -jar /usr/local/mcr1/mcr1.jar