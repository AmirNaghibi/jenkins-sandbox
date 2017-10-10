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