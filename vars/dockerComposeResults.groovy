def call(
        String service,
        String testReportsDir = null,
        boolean allowEmptyResults = false,
        double healthScaleFactor = 1.0
) {
    CONTAINER=sh(returnStdout: true, script: "docker-compose ps -q ${service}").trim()
    exit_code=sh(returnStdout: true, script: "docker wait ${CONTAINER}").trim()
    if (testReportsDir != null) {
        copyExitCode = sh(returnStatus: true, script: "docker cp ${CONTAINER}:${testReportsDir}/ ./results")
        if (copyExitCode != 0) {
            echo("WARNING: Fetching test results from container failed with ${copyExitCode}")
        } else {
            sh "find ./results -name '*.xml' -exec sed -i \"s:[[ATTACHMENT|${testReportsDir}:[[ATTACHMENT|\$(pwd)/results:g\" {} \\;"
        }
        junit(testResults: 'results/**/*.xml', allowEmptyResults: allowEmptyResults, healthScaleFactor: healthScaleFactor, testDataPublishers: [[$class: 'AttachmentPublisher']])
    }
    if (exit_code != "0") {
        error("Service ${service} exited with code ${exit_code}")
    }
}

def call(args) {
    return call(args.service, args.testReportsDir, args.allowEmptyResults || false, args.healtScaleFactor || 1.0)
}
