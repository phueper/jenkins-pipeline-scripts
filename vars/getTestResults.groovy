import hudson.tasks.test.AbstractTestResultAction

def call() {
    try {
        AbstractTestResultAction testResultAction = currentBuild.rawBuild.getAction(AbstractTestResultAction.class)

        if (testResultAction == null) return 'No tests found'

        def total = testResultAction.getTotalCount()
        def failed = testResultAction.getFailCount()
        def skipped = testResultAction.getSkipCount()

        return "${total - failed - skipped} tests succeeded, ${failed} failed, ${skipped} skipped".toString()
    } catch (e) {
        return "Cannot get test results: ${e}".toString()
    }
}

