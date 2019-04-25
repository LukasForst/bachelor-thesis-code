package pw.forst.olb.scheduler.server

import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.content.ByteArrayContent
import io.ktor.http.HttpMethod
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import pw.forst.olb.common.dto.server.ServerResponse
import pw.forst.olb.common.extensions.serialize

class ResponseHandler(private val httpClient: HttpClient) {

    fun handle(scope: CoroutineScope, response: ServerResponse) = scope.launch(Dispatchers.IO) {
        httpClient.call(response.reportEndpoint.url) {
            method = HttpMethod.parse(response.reportEndpoint.method)
            body = ByteArrayContent(serialize(response.allocationPlan))
        }
    }

    fun handle(response: ServerResponse) = runBlocking { handle(this, response) }
}
