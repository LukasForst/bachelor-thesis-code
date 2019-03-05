package pw.forst.olb.hub.client

import com.spotify.docker.client.DockerClient

interface ReleaseStrategy {

    /**
     * Indicates whether should be docker client released or not
     * */
    fun execute(client: DockerClient): Boolean
}
