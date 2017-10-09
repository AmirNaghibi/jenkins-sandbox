#!groovy

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
            def rootCommit = sh "git rev-list --max-parents=0 HEAD"

            for(String project : projects) {
                // find last tag
                def lastTag = sh(script:"git show-ref --hash --abbrev=7 refs/tags/lastbuild_${project} || echo ${rootCommit}", returnStdout: true).trim()

                // find current branch commitid
                def currentCommitId = sh(script:"git rev-parse --verify ${env.BRANCH_NAME}", returnStdout: true)
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
            for(String project : projectsToBuild) {
                echo "Building ${project}..."

                sh "mvn -f ${project}/pom.xml -DskipTests=true clean package"
            }
        }
      }
    }
    stage('Tagging') {
      steps {
        script {
            for(String project : projectsToBuild) {
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