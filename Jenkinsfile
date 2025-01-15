pipeline {
    agent any

    tools {
        maven 'Default Maven' // Use the Maven installation name you configured
    }

    environment {
        IMAGE_NAME = 'nour502/spring-app'
        DOCKER_USERNAME = 'nour502' // Hardcoded Docker Hub username
        DOCKER_PASSWORD = 'nourBF98?' // Hardcoded Docker Hub password
    }

    stages {
        stage('Checkout Code') {
            steps {
                // Checkout code from SCM
                checkout scm
            }
        }

        stage('Build with Maven') {
            steps {
                // Build the Spring Boot application with Maven
                sh 'mvn clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                // Build the Docker image
                sh "docker build -t ${IMAGE_NAME}:${env.BUILD_NUMBER} ."
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    // Authenticate to Docker Hub
                    sh '''
                        echo "${DOCKER_PASSWORD}" | docker login -u "${DOCKER_USERNAME}" --password-stdin
                    '''
                    // Push the Docker image to Docker Hub
                    sh "docker push ${IMAGE_NAME}:${env.BUILD_NUMBER}"
                    sh "docker tag ${IMAGE_NAME}:${env.BUILD_NUMBER} ${IMAGE_NAME}:latest"
                    sh "docker push ${IMAGE_NAME}:latest"
                }
            }
        }

        stage('Deploy Application') {
            steps {
                // Deploy the Docker container
                sh 'docker stop spring-app || true'
                sh 'docker rm spring-app || true'
                sh "docker run -d --name spring-app -p 8080:8080 ${IMAGE_NAME}:latest"
            }
        }
    }

    post {
        always {
            // Clean up the workspace
            cleanWs()
        }
    }
}
