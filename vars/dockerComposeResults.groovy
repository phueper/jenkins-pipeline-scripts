def call(
        String service,
        String testReportsDir = null,
        boolean allowEmptyResults = false,
        double healthScaleFactor = 1.0
) {
    CONTAINER=sh(returnStdout: true, script: "docker-compose ps -q ${service}").trim()
    exit_code=sh(returnStdout: true, script: "docker wait ${CONTAINER}").trim()
    if (testReportsDir != null) {
        sh "docker cp ${CONTAINER}:${testReportsDir}/ ./results"
        junit(testResults: 'results/**/*.xml', allowEmptyResults: allowEmptyResults, healthScaleFactor: healthScaleFactor, testDataPublishers: [[$class: 'AttachmentPublisher']])
    }
    if (exit_code != "0") {
        error("Service ${service} exited with code ${exit_code}")
    }
}
