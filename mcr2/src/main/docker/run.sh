#!/bin/sh
echo "********************************************************"
echo "Starting MCR2"
echo "********************************************************"
exec java -Djava.security.egd=file:/dev/./urandom -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -jar /usr/local/mcr2/mcr2.jar