#!groovy

String[] projects = ["mcr1", "mcr2", "mcr3"];

pipeline {
  agent any
  tools {
    maven 'Maven 3.5.0'
    jdk 'jdk8'
  }
  stages {
    stage('Build') {
      
      steps {
        script {
            for(String project : projects) {
                echo "Building ${project}..."

                sh "mvn -f ${project}/pom.xml clean package"
            }
        }
      }
    }
    stage('Test') {
      steps {
        echo 'Testing..'
      }
    }
    stage('Deploy') {
      steps {
        echo 'Deploying....'
      }
    }
  }
}