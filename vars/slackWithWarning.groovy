def call(String slackChannel, String slackJobDesc, String msg, Closure body) {
    try {
        return body()
    } catch (e) {
        slackSend(channel: slackChannel, color: 'warning', message: "${slackJobDesc}: ${msg}")
    }
}
