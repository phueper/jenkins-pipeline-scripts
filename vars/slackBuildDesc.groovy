String call() {
    def githubUrl = sh(returnStdout: true, script: 'git config --get remote.origin.url').trim().replaceFirst(/\.git$/, '')
    def commit = gitCommit()
    def jobUrl = env.JOB_DISPLAY_URL == null ? env.JOB_URL : env.JOB_DISPLAY_URL
    def runUrl = env.RUN_DISPLAY_URL == null ? currentBuild.absoluteUrl : env.RUN_DISPLAY_URL
    return "<${jobUrl}|${env.JOB_NAME}> <${runUrl}|${currentBuild.displayName}> for <${githubUrl}/commit/${commit}|${commit.take(6)}>".toString()
}
