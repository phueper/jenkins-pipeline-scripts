String call(String slackChannel, String buildDescription = slackBuildDesc()) {
    slackSend channel: slackChannel, message: "Starting ${buildDescription}\n<${env.RUN_CHANGES_DISPLAY_URL}|Changes:>\n${getChangeLog()}"
    return buildDescription
}
