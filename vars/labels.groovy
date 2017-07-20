import jenkins.model.*

@NonCPS
class labels {
    boolean isOnNodeWithLabel(String label) {
        Jenkins.instance.getLabels().contains(Jenkins.instance.getLabelAtom(label))
    }
}
