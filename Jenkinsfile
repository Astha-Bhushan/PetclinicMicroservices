pipeline {
  agent any
    tools {
      maven 'M2_HOME'
                 jdk 'JAVA_HOME'
    }
    stages {      
	
        stage('Build maven ') {
            steps { 
                    bat 'pwd'      
                    bat 'mvn  clean install package'
            }
        }
		
        stage("dockerize"){
            steps{
            bat "docker-compose up"
            }
        }
        
     
       
    }
}
