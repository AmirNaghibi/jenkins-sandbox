#!/usr/bin/env groovy

String[] projects = ["mcr1", "mcr2", "mcr3"];

def projectsToBuild = []

pipeline {
  agent any
  tools {
    maven 'Maven 3.5.0'
    jdk 'jdk8'
  }
  stages {
    stage('Diff') {
      
      steps {
        script {
            withCredentials([usernamePassword(credentialsId: 'github', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
                // fetch all tags
                sh("git fetch --tags || true")
            }

            def rootCommit = sh(script:"git rev-list --max-parents=0 HEAD", returnStdout:true)

            for(String project : projects) {
                // find last tag
                def lastTag = sh(script:"git show-ref --hash --abbrev=7 refs/tags/lastbuild_${env.BRANCH_NAME} || echo ${rootCommit}", returnStdout: true).trim()

                // find current branch commitid
                def currentCommitId = sh(script:"git rev-parse HEAD", returnStdout: true).trim()
                echo "currentCommitId = ${currentCommitId}"
                
                // find diff between current branch env.BRANCH_NAME and last build
                echo "Running git diff-tree --name-only ${currentCommitId}..${lastTag}  -- ${project}"
                def diff = sh(script:"git diff-tree --name-only ${currentCommitId}..${lastTag}  -- ${project}", returnStdout: true).trim()

                if (diff != "") {
                    projectsToBuild << project
                }
            }
        }
      }
    }
    stage('Build') {

      steps {
        script {
            if( projectsToBuild.isEmpty() ) {
                 echo "\u2600 Nothing to Build... \u2600"
            }

            for(String project : projectsToBuild) {
                echo "\u2600 Building ${project}... \u2600"

                sh "mvn -f ${project}/pom.xml -DskipTests=true clean package"
            }
        }
      }
    }
    stage('Tagging') {
      steps {
        script {
            if( projectsToBuild.isEmpty() == false ) {
                echo 'Tagging...'

                sh "git config user.email developer@nowherehub.org"
                sh "git config user.name developer"

                withCredentials([usernamePassword(credentialsId: 'github', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
                    // remove any existing lastbuild tag
                    sh("git tag -d lastbuild_${env.BRANCH_NAME} || true")
                    sh("git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/Nowheresly/jenkins-sandbox.git :refs/tags/lastbuild_${env.BRANCH_NAME}  || true")
                    
                    sh("git tag -a lastbuild_${env.BRANCH_NAME} -m 'Jenkins'")
                    sh('git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/Nowheresly/jenkins-sandbox.git --tags')
                }
                
            }
        }
      }
    }
  }
  post {
      always {
          echo 'Clean Up'
          deleteDir()
      }
  }
}