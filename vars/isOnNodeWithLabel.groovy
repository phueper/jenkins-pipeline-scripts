import jenkins.model.*

@NonCPS
def call(String label) {
    Jenkins.instance.getLabels().contains(Jenkins.instance.getLabelAtom(label))
}

