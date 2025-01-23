pipeline {

  agent any

  environment {

    WORKSPACE_DIR = '.'
    GRADLE_SETTINGS_FILE = '~/.gradle/gradle.properties'
  }

  parameters {

    gitParameter(
      name: 'branch',
      description: 'Branch being used for deployment',
      type: 'PT_BRANCH',
      branchFilter: 'origin/(.*)',
      defaultValue: 'origin/main',
      sortMode: 'ASCENDING_SMART',
      selectedValue: 'NONE',
      listSize: '10',
      quickFilterEnabled: true,
      requiredParameter: true
    )

    choice(
      name: 'library',
      description: 'Library being published',
      choices: [
        'api-library',
        'crypto-library',
        'dto-library',
        'i18n-library',
        'jpa-library',
        'logging-library',
        'mailer-library',
        'mariadb-library',
        'micrometer-library',
        'migrate-library',
        'mongodb-library',
        'mysql-library',
        'oracle-library',
        'postgresql-library',
        'rabbitmq-library',
        'redis-library',
        'sdk-library',
        'storage-library',
        'subscriber-library',
        'utilities-library',
        'validator-library'
      ]
    )
  }

  stages {

    stage("Validate") {
      steps {

        script {
          println("params.branch = " + params.branch)
          println("params.library = " + params.library)
          println("!params.branch = " + !params.branch)
          println("params.branch == null = " + params.branch == null)
          println("params.branch == '' = " + params.branch == '')
          println("!params.library = " + !params.library)
          println("params.library == null = " + params.library == null)
          println("params.library = '' = " + params.library == '')
          if (!params.branch) {
            error("Branch is required for deployment")
          } else if (!params.library) {
            error("Library is required for publishing")
          }
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
          credentialsId: 'gitlab-deploy-token',
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