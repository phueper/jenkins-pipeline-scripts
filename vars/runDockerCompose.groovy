import java.util.UUID

def call(
        String composeFileSuffix = '',
        String uuid = UUID.randomUUID(),
        Closure body = null
) {
    withEnv(["COMPOSE_FILE=docker-compose${composeFileSuffix}.yml",
             "COMPOSE_PROJECT_NAME=${uuid}"]) {
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
    return call(args.composeFileSuffix, args.uuid, body)
}
