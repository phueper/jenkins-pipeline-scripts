import jenkins.model.*

class labels extends Serializable {
    @NonCPS
    boolean isOnNodeWithLabel(String label) {
        Jenkins.instance.getLabels().contains(Jenkins.instance.getLabelAtom(label))
    }
}
