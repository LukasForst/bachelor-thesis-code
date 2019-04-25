package pw.forst.olb.scheduler.server

import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.content.ByteArrayContent
import io.ktor.http.HttpMethod
import kotlinx.coroutines.runBlocking
import mu.KLogging
import pw.forst.olb.common.dto.server.ServerResponse
import pw.forst.olb.common.extensions.serialize

class ResponseHandler(private val httpClient: HttpClient) {

    private companion object : KLogging()

    fun handle(response: ServerResponse) = runBlocking {
        httpClient.call(response.reportEndpoint.url) {
            method = HttpMethod.parse(response.reportEndpoint.method)
            body = ByteArrayContent(serialize(response.allocationPlan))
        }.also { logger.info { "Response: ${it.response.status}" } }
    }
}
