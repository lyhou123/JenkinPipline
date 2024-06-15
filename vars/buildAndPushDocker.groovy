def call(Map config = [:]) {
    def image = config.get('image', 'my-default-image')
    def registry = config.get('registry', 'my-default-registry')
    def tag = config.get('tag', 'latest')
    def credentialsId = config.get('credentialsId', 'dockerhub-credentials')

    // Write Dockerfile to the workspace
    def dockerfileContent = libraryResource 'angular.dockerfile'
    writeFile file: 'angular.dockerfile', text: dockerfileContent

    withCredentials([usernamePassword(credentialsId: credentialsId, passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
        sh """
            docker login -u ${USERNAME} -p ${PASSWORD}
            docker build -t ${registry}/${image}:${tag} -f angular.dockerfile .
            docker push ${registry}/${image}:${tag}
        """
    }
}
