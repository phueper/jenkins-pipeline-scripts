import java.util.Collection
import org.jenkins.plugins.lockableresources.LockableResourcesManager
import org.jenkins.plugins.lockableresources.LockableResource

Collection<LockableResource> getLockedResources() {
    LockableResourcesManager.class.get().getResourcesFromBuild(currentBuild.getRawBuild())
}

def call(String label, Closure body) {
    Collection<LockableResource> previous = getLockedResources()
    env.LOCKED_RESOURCE = null
    lock(label: label, quantity: 1) {
        Collection<LockableResource> current = getLockedResources()
        current.removeAll(previous)
        for (LockableResource res : current) {
            if (res.isValidLabel(label, null)) {
                env.LOCKED_RESOURCE = res.getName()
            }
        }

        return body()
    }
}
