package pw.forst.olb.hub.client

import com.spotify.docker.client.DefaultDockerClient
import com.spotify.docker.client.DockerClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KLogging
import pw.forst.olb.common.dto.docker.DockerHost
import pw.forst.olb.hub.execution.DockerContext
import kotlin.coroutines.CoroutineContext

/**
 * Uses default environment settings for building docker client
 * */
class DefaultDockerClientProvider(
    private var releaseStrategy: ReleaseStrategy,
    override val coroutineContext: CoroutineContext
) : DockerContext, DockerClientProvider {

    private companion object : KLogging()

    private val cache: MutableMap<DockerHost, Pair<DockerClient, Int>> = mutableMapOf()

    //will be used in the future
    private fun create(@Suppress("UNUSED_PARAMETER") host: DockerHost): DockerClient = DefaultDockerClient
        .fromEnv()
//        .uri(host.uri)
        .build()


    private suspend fun verify(client: DockerClient): DockerClient? = withContext(Dispatchers.IO) {
        client.ping().let {
            if (it == "OK") client
            else null.also { logger.error { "It was not possible to reach docker host! Ping result - $it" } }
        }
    }

    /**
     * Returns docker client for given docker host configuration or null when docker host does not respond to the ping
     * */
    override suspend fun getClient(host: DockerHost): DockerClient? { //TODO make this thread safe
        val (client, usage) = cache[host] ?: create(host).also { logger.info { "New client for $host was created." } } to 0

        if (usage == 0 && verify(client) == null) return null

        cache[host] = client to usage + 1
        return client
    }


    override fun returnClient(host: DockerHost) { //TODO make this thread safe
        val (client, usage) = cache[host]
            ?: logger.error { "Some process tried to return client that does not exist! This should not happen! $host" }.run { return }

        if (usage - 1 == 0 && releaseStrategy.execute(client)) client.close().also { cache.remove(host); logger.info { "Client for docker $host was closed" } }
        else cache[host] = client to usage - 1
    }

    override fun setDockerClientReleaseStrategy(releaseStrategy: ReleaseStrategy) {
        this.releaseStrategy = releaseStrategy
    }
}
