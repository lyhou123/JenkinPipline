def call() {
    def testExecution = libraryResource 'scripts/hello.sh'
    writeFile file: 'hello.sh', text: testExecution
    sh "chmod +x hello.sh"
    sh "bash hello.sh"
    // sh 'docker build -t myapp:latest .'
}