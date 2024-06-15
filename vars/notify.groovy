import org.devops.Notification

def call(Map params) {
    def dockerfileContent = libraryResource 'docker/angular.dockerfile'
    writeFile file: 'angular.dockerfile', text: dockerfileContent

    try {
        withCredentials([usernamePassword(credentialsId: env.REGISTRY_CREDENTIALS_ID, passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
            // docker build
            def imageFull = dockerBuild(username: USERNAME, 
                                        imageName: env.IMAGENAME, 
                                        tag: env.TAG, 
                                        registryName: env.REGISTRY_NAME)
            
            // docker push
            if (env.REGISTRY_NAME == 'docker.io') {
                sh "docker login -u ${USERNAME} -p ${PASSWORD}"
            } else {
                sh "docker login -u ${USERNAME} -p ${PASSWORD} ${env.REGISTRY_NAME}"
            }
            sh "docker push ${imageFull}"

            // If everything above is successful, send a success notification
            def notify = new Notification(steps, this)
            notify.sendTelegram("Build success✅ Image: ${imageFull}")
        }
    } catch (Exception e) {
        def notify = new Notification(steps, this)
        notify.sendTelegram("Build failed⛔ Error: ${e.getMessage()}")
        echo "Build failed⛔ Error: ${e.getMessage()}"
        currentBuild.result = 'FAILURE'
        throw e
    }
}

def String dockerBuild(Map params) {
    def dockerImage = "${params.registryName == 'docker.io' ? '' : params.registryName + '/'}${params.username}/${params.imageName}:${params.tag}"
    sh "docker build -t ${dockerImage} -f angular.dockerfile ."
    return dockerImage
}


// import org.devops.Notification

// def call(Map params) {
//     def dockerfileContent = libraryResource 'docker/angular.dockerfile'
//     writeFile file: 'angular.dockerfile', text: dockerfileContent

//     try {
//         withCredentials([usernamePassword(credentialsId: env.REGISTRY_CREDENTIALS_ID, passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
//             // docker build
//             def imageFull = dockerBuild(username: USERNAME, 
//                                         imageName: env.IMAGENAME, 
//                                         tag: env.TAG, 
//                                         registryName: env.REGISTRY_NAME)
            
//             // docker push
//             if (env.REGISTRY_NAME == 'docker.io') {
//                 sh "docker login -u ${USERNAME} -p ${PASSWORD}"
//             } else {
//                 sh "docker login -u ${USERNAME} -p ${PASSWORD} ${env.REGISTRY_NAME}"
//             }
//             sh "docker push ${imageFull}"
//         }
//     } catch (Exception e) {
//         def notify = new Notification(steps, this)
//         notify.sendTelegram("Build failed⛔(<:>) Error: ${e.getMessage()}")
//         echo "Build failed⛔(<:>) Error: ${e.getMessage()}"
//         currentBuild.result = 'FAILURE'
//         throw e
//     }
// }

// def String dockerBuild(Map params) {
//     def dockerImage = "${params.registryName == 'docker.io' ? '' : params.registryName + '/'}${params.username}/${params.imageName}:${params.tag}"
//     sh "docker build -t ${dockerImage} -f angular.dockerfile ."
//     def notify = new Notification(steps, this)
//     notify.sendTelegram("Build success✅(<:>) Image: ${dockerImage}")
//     sh "echo ${dockerImage}"
//     // docker rm $(docker ps -a -q)
//     // docker rmi $(docker images -q)
//     sh "docker rmi -f ${dockerImage}"
//     return dockerImage
// }
