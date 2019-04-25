package pw.forst.olb.scheduler.client

import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.content.ByteArrayContent
import io.ktor.http.HttpMethod
import kotlinx.coroutines.runBlocking
import mu.KLogging
import pw.forst.olb.common.dto.server.NewPlanCreationRequest
import pw.forst.olb.common.dto.server.PlanEnhancementRequest
import pw.forst.olb.common.extensions.serialize

class RequestSender(
    private val httpClient: HttpClient,
    private val baseUrl: String
) {
    private companion object : KLogging()

    fun handle(request: NewPlanCreationRequest) {
        runBlocking {
            httpClient.call("$baseUrl/new-plan") {
                method = HttpMethod.Post
                body = ByteArrayContent(serialize(request))
            }.also { logger.info { "Response: ${it.response.status}" } }
        }
    }

    fun handle(request: PlanEnhancementRequest) {
        runBlocking {
            httpClient.call("$baseUrl/enhance-plan") {
                method = HttpMethod.Post
                body = ByteArrayContent(serialize(request))
            }.also { logger.info { "Response: ${it.response.status}" } }
        }
    }

}
