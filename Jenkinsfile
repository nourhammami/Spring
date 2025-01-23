pipeline {
    agent any

    tools {
        maven 'Default Maven' // Use the configured Maven installation
    }

    environment {
        IMAGE_NAME = 'nour502/spring-app'
        DOCKER_USERNAME = 'nour502' 
        DOCKER_PASSWORD = 'nourBF98?' 
        SONARQUBE_SERVER = 'SonarQube' // Use the name configured in Jenkins
        SONARQUBE_TOKEN = credentials('sonarqube-token') // Store this token in Jenkins credentials
    }

    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Unit Tests') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml' // Collect JUnit test reports
                }
            }
        }

        stage('SonarQube Analysis') {
    steps {
        script {
            withSonarQubeEnv('SonarQube') {
                sh 'mvn sonar:sonar -Dsonar.projectKey=my-project -Dsonar.host.url=http://host.docker.internal:9000 -Dsonar.token=$SONARQUBE_TOKEN'
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
                #!/bin/bash
                echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
                docker tag ${IMAGE_NAME}:${env.BUILD_NUMBER} ${IMAGE_NAME}:latest
                docker push ${IMAGE_NAME}:${env.BUILD_NUMBER}
                docker push ${IMAGE_NAME}:latest
            '''
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
