node {
    stage 'Clone the project'
    git 'https://github.com/ksbrwsk/reactive-talk-202012-kotlin.git'

    stage("Compilation and Analysis") {
        parallel 'Compilation': {
            sh "./mvnw clean install -DskipTests"
        }
    }

    stage("Tests and Deployment") {
        parallel 'Unit tests': {
            stage("Running unit tests") {
                sh "./mvnw test -Punit"
            }
        }
    }

    stage("Staging") {
        sh "pid=\$(lsof -i:8989 -t); kill -TERM $pid || kill -KILL $pid"
        withEnv(['JENKINS_NODE_COOKIE=dontkill']) {
            sh 'nohup ./mvnw spring-boot:run -Dserver.port=8989 &'
        }
    }
}