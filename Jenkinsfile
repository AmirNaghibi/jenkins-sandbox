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

                sh "mvn -f ${project}/pom.xml -DskipTests=true clean package"
            }
        }
      }
    }
    stage('Tagging') {
      steps {
        script {
            for(String project : projects) {
                echo 'Tagging ${project}...'

                sh "git config user.email developer@nowherehub.org"
                sh "git config user.name developer"

                withCredentials([usernamePassword(credentialsId: 'github', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
                    // remove any existing lastbuild tag
                    sh("git tag -d lastbuild_${project} || true")
                    sh("git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/Nowheresly/jenkins-sandbox.git :refs/tags/lastbuild_${project}  || true")
                    
                    sh("git tag -a lastbuild_${project} -m 'Jenkins'")
                    sh('git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/Nowheresly/jenkins-sandbox.git --tags')
                }
                
            }
        }
      }
    }
    stage('Deploy') {
      steps {
        echo 'Deploying....'
      }
    }
  }
}