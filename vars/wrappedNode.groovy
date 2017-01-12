def call(Map vars, Closure body=null) {
  vars = vars ?: [:]
  node(vars.get("label", null)) {
    withDockerRegistry(url: vars.get("registryUrl", "https://index.docker.io/v1/"), credentialsId: vars.get("registryCreds", "aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaca")) {
      wrap([$class: 'TimestamperBuildWrapper']) {
        wrap([$class: 'AnsiColorBuildWrapper']) {
          if (vars.get('cleanWorkspace', false)) {
            // NOTE: `withChownWorkspace` uses docker. if our `label` doesn't have docker
            // or is misconfigured, these operations will fail and the exception will be
            // propogated.
            withChownWorkspace { echo "cleanWorkspace: Ensuring workspace is owned by ${env.USER}" }
            echo "cleanWorkspace: Removing existing workspace"
            deleteDir()
            echo "cleanWorkspace: Workspace is clean."
          }
          if (body) { body() }
        }
      }
    }
  }
}
