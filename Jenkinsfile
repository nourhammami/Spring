pipeline {
    agent any

    tools {
        maven 'Default Maven' // Use the Maven name you configured in Jenkins
    }

    environment {
        IMAGE_NAME = 'nour502/spring-app'
        DOCKER_REGISTRY = 'https://index.docker.io/v1/'
    }

    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    sh "docker build -t ${IMAGE_NAME}:${env.BUILD_NUMBER} ."
                }
            }
        }

        stage('Docker Push') {
            steps {
                script {
                    docker.withRegistry(DOCKER_REGISTRY, 'dockerhub-credentials-id') {
                        sh "docker push ${IMAGE_NAME}:${env.BUILD_NUMBER}"
                        sh "docker tag ${IMAGE_NAME}:${env.BUILD_NUMBER} ${IMAGE_NAME}:latest"
                        sh "docker push ${IMAGE_NAME}:latest"
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    sh 'docker stop spring-app || true'
                    sh 'docker rm spring-app || true'
                    sh "docker run -d --name spring-app -p 8080:8080 ${IMAGE_NAME}:latest"
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
