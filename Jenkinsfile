pipeline {
  agent any
    tools {
      maven 'M2_HOME'
                 jdk 'JAVA_HOME'
    }
    stages {      
	
        stage('Build maven ') {
            steps { 
                    sh 'pwd'      
                    sh 'mvn  clean install package'
            }
        }
		
        stage("dockerize"){
            steps{
            sh "docker-compose up"
            }
        }
        
     
       
    }
}
