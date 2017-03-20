@NonCPS
def call() {
    return (env.JENKINS_URL =~ /^((http[s]?|ftp):\/)?\/?([^:\/\s]+)/)[0][3]
}

