String after() {
    return " after ${currentBuild.durationString.replace(' and counting', '')}".toString()
}

def call(String slackChannel, String buildDescription, Throwable error = null) {
    boolean unstable = currentBuild.resultIsBetterOrEqualTo('UNSTABLE')
    boolean success = currentBuild.resultIsBetterOrEqualTo('SUCCESS')
    String color = error != null ? 'danger' : success ? 'good' : unstable ? 'warning' : 'danger'
    String message = error != null ? "FAILED: ${error}${after()}"
            : success ? "ALL Tests succeeded${after()}"
            : "${currentBuild.currentResult}${after()}\n${getTestSummary()}"
    slackSend(channel: slackChannel, color: color, message: "${buildDescription}: ${message}")
}
