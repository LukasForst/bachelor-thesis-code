package pw.forst.olb.scheduler.client.routes

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.post
import mu.KLogging
import pw.forst.olb.common.dto.AllocationPlan
import pw.forst.olb.scheduler.client.SchedulingQueue
import pw.forst.olb.server.api.routes.configuration.Route
import pw.forst.olb.server.api.routes.configuration.RouteBase
import pw.forst.olb.server.extensions.receiveInBytes

@Route
class CoreResponseRoute(schedulingQueue: SchedulingQueue) : RouteBase("response") {

    private companion object : KLogging()

    init {
        route {
            post("/{id}") {
                logger.info { "Response endpoint called" }
                val schedulingId = call.parameters["id"]?.toIntOrNull() ?: throw IllegalArgumentException("Wrong id parameter! Int is expected, actual: ${call.parameters["id"]}")

                logger.info { "Parsing allocation plan with scheduling id $schedulingId" }

                val request = call.receiveInBytes<AllocationPlan>()

                logger.info { "Plan $schedulingId parsed." }
                schedulingQueue.receive(schedulingId, request)

                logger.info { "Sending respond to client" }
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
