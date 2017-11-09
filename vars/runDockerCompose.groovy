def call(
        String composeFileSuffix = '',
        String composeProjectName = UUID.randomUUID(),
        String composeFile,
        String service,
        Closure body = null
) {
    if (!composeFileSuffix) composeFileSuffix = ''
    if (!composeProjectName) composeProjectName = UUID.randomUUID()
    if (!composeFile) composeFile = "docker-compose${composeFileSuffix}.yml"
    String flags = service ? "--exit-code-from ${service}" : '--abort-on-container-exit'
    withEnv(["COMPOSE_FILE=${composeFile}",
             "COMPOSE_PROJECT_NAME=${composeProjectName}",
             "PATH+WHATEVER=${tool('docker-compose')}"]) {
        ansiColor('xterm') {
            try {
                sh "docker-compose create"
                lock(label: 'docker', quantity: 1) {
                    sh "docker-compose up -d"
                }
                sh "docker-compose up ${flags}"
                if (body) {
                    return body()
                }
            } finally {
                sh "docker-compose down --volumes"
            }
        }
    }
}

def call(args, Closure body = null) {
    return call(args.composeFileSuffix, args.composeProjectName, args.composeFile, args.service, body)
}
