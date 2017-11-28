# Microservices proof-of-concept

A microserice sample using spring boot, docker, traefik, ELK...

# Pipeline-As-Code using Jenkins with Microservices

## Context
Let's say you want to develop some microservices.
To avoid creating too much git repositories, you decide to put all of them in a single git repository.

As CI-CD tool, you decide to use [Jenkins](https://jenkins.io/) with its feature: pipeline-as-code.

Now come the bad news: so far [jenkins does not support several Jenkinsfiles per repository](https://issues.jenkins-ci.org/browse/JENKINS-43749).

This project is an attempt to bypass this current limitation.

## Solution
This idea is to have a single Jenkinsfile at the root of the repository.

When a given microservice is updated, this Jenkinsfile will perform a diff to identify
which microservice is impacted by the changeset and will trigger only the build for those microservices.

At the end of the build, a dedicated tag (`lastbuild_<branchname>`) is pushed to the repository so that it would be possible to
calculate the changeset with the previous successful build.


# Docker
## How to build docker images
for each project

`mvn clean package docker:build` 

# Traefik
All microservices mcr* are behind a reverse proxy called [Traefik](https://docs.traefik.io/).

Start the compose file:

`docker-scripts/docker-compose up`

Or better, use swarm:

`docker swarm init`

`docker stack deploy -c ./docker-scripts/docker-compose.yml mcr`

Test it :
- [http://localhost:80/mcr1/health](http://localhost:80/mcr1/health)
- [http://localhost:80/mcr2/health](http://localhost:80/mcr2/health)
- [http://localhost:80/mcr3/health](http://localhost:80/mcr3/health)

# Logging
In order to activate logging in an ELK stack, you need to deploy `docker-compose-logging.yml` in a docker stack.
Follow the steps below:

## How to create your swarm?
Run this command: 

`docker swarm init`

## How to create logstash config?
Run this command: 

`docker config create logstash.conf ./etc/logstash.conf`

## How to create the log network?
Run this command: 

`docker network create -d overlay log`

## How to deploy the logging stack?
Run this command: 

`docker stack deploy -c ./docker-scripts/docker-compose-logging.yml log`

A new stack named `log` is created.

## How to remove the logging stack?
Run this command: 

`docker stack rm log`

The stack named `log` is removed.

