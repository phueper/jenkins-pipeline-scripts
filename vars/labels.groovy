import jenkins.model.*

@NonCPS
boolean isOnNodeWithLabel(String label) {
    Jenkins.instance.getLabels().contains(Jenkins.instance.getLabelAtom(label))
}
