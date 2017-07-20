String after() {
    return " after ${currentBuild.durationString.replace(' and counting', '')}".toString()
}

def call(String slackChannel, String buildDescription, Throwable error = null) {
    boolean unstable = error == null && currentBuild.resultIsBetterOrEqualTo('UNSTABLE')
    boolean success = !unstable && currentBuild.resultIsBetterOrEqualTo('SUCCESS')
    String color = success ? 'good' : unstable ? 'warning' : 'danger'
    String message = error != null ? "FAILED: ${error}${after()}"
            : success ? "ALL Tests succeeded${after()}"
            : "${currentBuild.currentResult}${after()}\\n${getTestSummary()}"
    slackSend(channel: slackChannel, color: color, message: "${buildDescription}: ${message}")
}
