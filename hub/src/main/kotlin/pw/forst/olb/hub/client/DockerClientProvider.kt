package pw.forst.olb.hub.client

import com.spotify.docker.client.DockerClient
import pw.forst.olb.common.dto.docker.DockerHost

/**
 * Provides access to [DockerClient] with given [DockerHost]
 * */
interface DockerClientProvider {

    /**
     * Returns docker client for given docker host configuration or null when docker host does not respond to the ping
     * */
    suspend fun getClient(host: DockerHost): DockerClient?

    /**
     * Returns client to the pool
     * */
    fun returnClient(host: DockerHost)

    /**
     * Sets release strategy for closing docker client connection
     * */
    fun setDockerClientReleaseStrategy(releaseStrategy: ReleaseStrategy)
}
