import hudson.tasks.test.AbstractTestResultAction

def call() {
    try {
        AbstractTestResultAction testResultAction = currentBuild.rawBuild.getAction(AbstractTestResultAction.class)

        if (testResultAction == null) return 'No tests found'

        def total = testResultAction.getTotalCount()
        def failed = testResultAction.getFailCount()
        def skipped = testResultAction.getSkipCount()

        if (skipped == 0) {
            if (failed == 0) {
                return "All ${total} tests succeeded".toString()
            }
            return "${total - failed} tests succeeded, ${failed} failed".toString()
        }
        if (failed == 0) {
            return "${total - skipped} tests succeeded, ${skipped} skipped".toString()
        }
        return "${total - failed - skipped} tests succeeded, ${failed} failed, ${skipped} skipped".toString()
    } catch (e) {
        return "Cannot get test results: ${e}".toString()
    }
}

