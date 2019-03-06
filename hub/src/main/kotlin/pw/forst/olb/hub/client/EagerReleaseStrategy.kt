package pw.forst.olb.hub.client

import com.spotify.docker.client.DockerClient

/**
 * Release client every time
 * */
class EagerReleaseStrategy : ReleaseStrategy {

    override fun execute(client: DockerClient): Boolean = true

}
