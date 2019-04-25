package pw.forst.olb.scheduler.client

import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.content.ByteArrayContent
import io.ktor.http.HttpMethod
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import pw.forst.olb.common.dto.server.NewPlanCreationRequest
import pw.forst.olb.common.dto.server.PlanEnhancementRequest
import pw.forst.olb.common.extensions.serialize

class RequestSender(
    private val httpClient: HttpClient,
    private val baseUrl: String
) {
    fun handle(request: NewPlanCreationRequest) {
        runBlocking { handle(this, request) }
    }

    fun handle(scope: CoroutineScope, request: NewPlanCreationRequest) = scope.launch(Dispatchers.IO) {
        httpClient.call("$baseUrl/new-plan") {
            method = HttpMethod.Post
            body = ByteArrayContent(serialize(request))
        }
    }

    fun handle(request: PlanEnhancementRequest) {
        runBlocking { handle(this, request) }
    }

    fun handle(scope: CoroutineScope, request: PlanEnhancementRequest) = scope.launch(Dispatchers.IO) {
        httpClient.call("$baseUrl/enhance-plan") {
            method = HttpMethod.Post
            body = ByteArrayContent(serialize(request))
        }
    }
}
