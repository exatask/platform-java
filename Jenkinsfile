@Library("groovy-jenkins-library") _

import com.exatask.*

pipeline {

  agent any

  environment {

    WORKSPACE_DIR = '.'
    GRADLE_SETTINGS_FILE = '~/.gradle/gradle.properties'
  }

  // parameters {

//     activeChoice(
//         name: 'library',
//         description: 'Select the library to be published',
//         choiceType: 'PT_SINGLE_SELECT',
//         script: [
//           $class: 'GroovyScript',
//           script: [
//             sandbox: true,
//             script: """println("Executing the script to list directories")
// try {
//   def gitUtilities = new GitUtilities()
//   def data = gitUtilities.listDirectories("git@gitlab.com:exatask/platform/platform-java.git", "main")
//   println("Directories loaded: {0}", data)
//   return data
// } catch (err) {
//   println(err)
// }"""
//           ],
//           fallbackScript: [
//             sandbox: true,
//             script: """return ["No directories found"]"""
//           ]
//         ]
//     )

//    choice(
//      name: 'library',
//      description: 'Select the library to be published',
//      choices: getDirectories()
//    )
  // }

  stages {

    stage("Setup") {
      steps {

        script {
          properties([
            parameters([
              [
                $class: 'CascadeChoiceParameter',
                choiceType: 'PT_SINGLE_SELECT',
                name: 'LIBRARY',
                filterable: true,
                script: [
                  $class: 'GroovyScript',
                  script: [
                    sandbox: true,
                    script: '''
println("Executing the script to list directories")
try {
 def gitUtilities = new GitUtilities()
 def data = gitUtilities.listDirectories("git@gitlab.com:exatask/platform/platform-java.git", "main")
 println("Directories loaded: {0}", data)
 return data
} catch (err) {
 println(err)
 return ["There is nothing"]
}'''
                   ]
                ]
              ]
            ])
          ])
        }
      }
    }

    stage("Checkout") {
      steps {

        echo "Checking out the repository..."
        checkout scm
      }
    }

    stage("Setup Gradle") {
      steps {

        echo "Configuring gradle properties..."
        withCredentials([string(
          credentialsId: 'gitlab-deploy-token-id',
          variable: 'GITLAB_DEPLOY_TOKEN'
        )]) {

          sh """
          echo "gitlabDeployToken=${GITLAB_DEPLOY_TOKEN}" >> ${GRADLE_SETTINGS_FILE}
          """
        }
      }
    }

    stage("Test") {
      steps {

        echo "Running tests..."
        sh './../gradlew :${params.library}:test'
      }
    }

    stage("Build") {
      steps {

        echo "Building library..."
        sh './../gradlew :${params.library}:clean :${params.library}:build -x test'
      }
    }

    stage("Publish") {
      steps {

        echo "Publishing JAR to repository..."
        sh './../gradlew :${params.library}:publish'
      }
    }
  }

  post {
    success {
      echo "Build and publishing completed successfully"
    }
    failure {
      echo "Build or publishing failed"
    }
//    always {
//     cleanWs()
//    }
  }
}

def getDirectories() {

  node {
    def directories = []
    def result = sh(script: "ls -d ${env.WORKSPACE}/*-library/ | sed 's|/||'", returnStdout: true).trim()
    result.split('\n').each { directory ->
      directories << directory
    }
    return directories
  }
}