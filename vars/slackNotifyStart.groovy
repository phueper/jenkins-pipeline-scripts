String call(String slackChannel, String buildDescription = slackBuildDesc()) {
    slackSend(
            channel: slackChannel,
            message: "Starting ${buildDescription} <${env.RUN_CHANGES_DISPLAY_URL}|(Changes)>")
    return buildDescription
}
