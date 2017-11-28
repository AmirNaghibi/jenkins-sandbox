#!/bin/sh
echo "********************************************************"
echo "Starting MCR3"
echo "********************************************************"
exec java -Djava.security.egd=file:/dev/./urandom -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -jar /usr/local/mcr3/mcr3.jar