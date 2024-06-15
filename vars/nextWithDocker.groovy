def call(String DOCKER_TAG, String ImageName) {
    def dockerfileContent = libraryResource 'next.dockerfile'
    writeFile file: 'Dockerfile', text: dockerfileContent
    sh "docker build -t ${ImageName}:${DOCKER_TAG} ."
    sh "echo ${DOCKER_TAG}"
} 