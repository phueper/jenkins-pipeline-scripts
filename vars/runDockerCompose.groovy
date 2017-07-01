import java.util.UUID

def call(
        String composeFileSuffix = '',
        String composeProjectName = UUID.randomUUID(),
        String composeFile,
        Closure body = null
) {
    if (!composeFileSuffix) composeFileSuffix = ''
    if (!composeProjectName) composeProjectName = UUID.randomUUID()
    if (!composeFile) composeFile = "docker-compose${composeFileSuffix}.yml"
    withEnv(["COMPOSE_FILE=${composeFile}",
             "COMPOSE_PROJECT_NAME=${composeProjectName}"]) {
        ansiColor('xterm') {
            try {
                sh "docker-compose config && docker-compose up --abort-on-container-exit"
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
    return call(args.composeFileSuffix, args.composeProjectName, args.composeFile, body)
}
