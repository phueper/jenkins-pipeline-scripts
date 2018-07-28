def call(
        String service,
        String testReportsDir = null,
        boolean allowEmptyResults = false,
        double healthScaleFactor = 1.0,
        def perfReportConstraints = null
) {
    docker.withTool('docker') {
        def CONTAINER = sh(returnStdout: true, script: "docker-compose ps -q ${service}").trim()
        def exit_code = sh(returnStdout: true, script: "docker wait ${CONTAINER}").trim()
        if (testReportsDir != null) {
            def copyExitCode = sh(returnStatus: true, script: $/
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
    def buildResultBeforePerfReport = currentBuild.currentResult
    if (perfReportConstraints != null) {
        perfReport(
                sourceDataFiles: 'results/**/*.xml',
                constraints: perfReportConstraints,
                modeEvaluation: true,
        )
        def buildResultAfterPerfReport = currentBuild.currentResult
        if (buildResultAfterPerfReport == 'FAILURE' && (buildResultBeforePerfReport != buildResultAfterPerfReport)) {
            error("Performance Report failed")
        } else {
            echo "Performance Report succeeded"
        }
    } else {
        echo "no perfReportConstraints parameter, skipped Performance Report generation/validation"
    }
}

def call(args) {
        return call(args.service, args.testReportsDir, args.allowEmptyResults || false, (args.healthScaleFactor ?: 1.0d), args.perfReportConstraints ?: null)
}

