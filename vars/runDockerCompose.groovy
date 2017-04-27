import java.util.UUID

def call(
        String composeFileSuffix = '',
        String composeProjectName = UUID.randomUUID(),
        Closure body = null
) {
    if (!composeFileSuffix) composeFileSuffix = ''
    if (!composeProjectName) composeProjectName = UUID.randomUUID()
    withEnv(["COMPOSE_FILE=docker-compose${composeFileSuffix}.yml",
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
    return call(args.composeFileSuffix, args.composeProjectName, body)
}
