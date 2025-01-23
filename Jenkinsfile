pipeline {
    agent any

    tools {
        maven 'Default Maven'
    }

    environment {
        IMAGE_NAME = 'nour502/spring-app'
        DOCKER_USERNAME = 'nour502'
        DOCKER_PASSWORD = 'nourBF98?'
        SONARQUBE_SERVER = 'SonarQube'
        SONARQUBE_TOKEN = credentials('sonarqube-token')
    }

    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Unit Tests') {
    steps {
        script {
            try {
                // ✅ Correct Maven commands
                sh 'mvn clean test'
                sh 'mvn verify'  // Ensures JaCoCo reports are generated
            } finally {
                // ✅ Always collect test and coverage reports
                junit '**/target/surefire-reports/*.xml'
                jacoco execPattern: '**/target/jacoco.exec'
            }
        }
    }
    post {
        success {
            echo '✅ Unit tests and coverage passed!'
        }
        failure {
            echo '❌ Unit tests failed. Stopping pipeline.'
            error 'Stopping pipeline due to test failures.'
        }
    }
}


        stage('SonarQube Analysis') {
            steps {
                script {
                    withSonarQubeEnv('SonarQube') {
                        sh 'mvn sonar:sonar -Dsonar.projectKey=my-project -Dsonar.host.url=http://host.docker.internal:9000 -Dsonar.token=$SONARQUBE_TOKEN -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml'
                    }
                }
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${IMAGE_NAME}:${env.BUILD_NUMBER} ."
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    sh '''
                        echo "${DOCKER_PASSWORD}" | docker login -u "${DOCKER_USERNAME}" --password-stdin
                    '''
                    sh "docker push ${IMAGE_NAME}:${env.BUILD_NUMBER}"
                    sh "docker tag ${IMAGE_NAME}:${env.BUILD_NUMBER} ${IMAGE_NAME}:latest"
                    sh "docker push ${IMAGE_NAME}:latest"
                }
            }
        }

        stage('Deploy Application') {
            steps {
                sh 'docker stop spring-app || true'
                sh 'docker rm spring-app || true'
                sh "docker run -d --name spring-app -p 83:8080 ${IMAGE_NAME}:latest"
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
