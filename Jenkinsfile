pipeline {
    agent any

    tools {
        maven 'Maven 3.x' // Must match the name configured in Jenkins Global Tool Configuration
        jdk 'Java 17'     // Must match the JDK configured in Jenkins Global Tool Configuration
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Clean & Compile') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Run Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Package & Verify') {
            steps {
                sh 'mvn verify'
            }
        }
    }

    post {
        always {
            junit '**/target/surefire-reports/*.xml'
        }
        success {
            echo 'Build and test verification passed successfully!'
        }
        failure {
            echo 'Pipeline failed. Check test execution summaries.'
        }
    }
}
