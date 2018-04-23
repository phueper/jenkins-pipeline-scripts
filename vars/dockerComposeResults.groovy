def call(
        String service,
        String testReportsDir = null,
        boolean allowEmptyResults = false,
        double healthScaleFactor = 1.0
) {
    docker.withTool('docker') {
        CONTAINER = sh(returnStdout: true, script: "docker-compose ps -q ${service}").trim()
        exit_code = sh(returnStdout: true, script: "docker wait ${CONTAINER}").trim()
        if (testReportsDir != null) {
            copyExitCode = sh(returnStatus: true, script: $/
docker cp ${CONTAINER}:${testReportsDir}/ ./results
SED_COMMAND="s:$(echo -n "[[ATTACHMENT|${testReportsDir}" | sed 's/[\[\:&.*]/\\&/g'):$(echo -n "[[ATTACHMENT|$(pwd)/results" | sed 's/[:&]/\\&/g'):g"
find ./results -name '*.xml' -exec sed -i "$${SED_COMMAND}" {} \;
/$)
            if (copyExitCode != 0) {
                echo("WARNING: Fetching test results from container failed with ${copyExitCode}")
            }
            junit(testResults: 'results/**/*.xml', allowEmptyResults: allowEmptyResults, healthScaleFactor: healthScaleFactor, testDataPublishers: [[$class: 'AttachmentPublisher']])
        }
        if (exit_code != "0") {
            error("Service ${service} exited with code ${exit_code}")
        }
    }
}

def call(args) {
        return call(args.service, args.testReportsDir, args.allowEmptyResults, args.healthScaleFactor)
}

