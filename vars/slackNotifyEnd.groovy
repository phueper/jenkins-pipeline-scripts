String after() {
    return " after ${currentBuild.durationString.replace(' and counting', '')}\n<${env.RUN_CHANGES_DISPLAY_URL}|Changes:>\n${getChangeLog()}".toString()
}

void unstableOrWorse(String slackChannel, String buildDescription) {
    String color = currentBuild.currentResult == 'UNSTABLE' ? 'warning' : 'danger'
    String message = "${currentBuild.currentResult}${after()}\n${getTestSummary()}"
    slackSend(channel: slackChannel, color: color, message: "${buildDescription}: ${message}")
}

void success(String slackChannel, String buildDescription) {
    slackSend(channel: slackChannel, color: 'good', message: "${buildDescription}: ALL Tests succeeded${after()}")
}

void buildError(String slackChannel, String buildDescription, Throwable error) {
    slackSend(channel: slackChannel, color: 'danger', message: "${buildDescription}: FAILED: ${error}${after()}")
}

def call(String slackChannel, String buildDescription, Throwable error = null) {
    if (error != null) {
        buildError(slackChannel, buildDescription, error)
    } else if (currentBuild.currentResult == 'SUCCESS') {
        success(slackChannel, buildDescription, )
    } else {
        unstableOrWorse(slackChannel, buildDescription)
    }
}
