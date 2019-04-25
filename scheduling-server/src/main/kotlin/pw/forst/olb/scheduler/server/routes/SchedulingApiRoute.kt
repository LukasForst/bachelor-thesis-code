package pw.forst.olb.scheduler.server.routes

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.post
import mu.KLogging
import pw.forst.olb.common.dto.server.NewPlanCreationRequest
import pw.forst.olb.common.dto.server.PlanEnhancementRequest
import pw.forst.olb.common.dto.server.ServerResponse
import pw.forst.olb.scheduler.server.ResponseHandler
import pw.forst.olb.scheduler.server.SchedulingQueue
import pw.forst.olb.server.api.routes.configuration.Route
import pw.forst.olb.server.api.routes.configuration.RouteBase
import pw.forst.olb.server.extensions.receiveInBytes

@Route
class SchedulingApiRoute(
    private val schedulingQueue: SchedulingQueue,
    private val responseHandler: ResponseHandler
) : RouteBase("scheduling") {

    private companion object : KLogging()

    init {
        route {
            post("new-plan") {
                logger.info { "New plan endpoint called" }

                val request = call.receiveInBytes<NewPlanCreationRequest>()

                logger.info { "New plan creation request parsed, executing scheduling!" }

                schedulingQueue.execute(request) {
                    logger.info { "Scheduling ended, creating response." }
                    responseHandler.handle(ServerResponse(request.reportEndpoint, it))
                    logger.info { "Response sent!" }
                }

                logger.info { "Scheduling enqueued!" }
                call.respond(HttpStatusCode.OK)
            }
            post("enhance-plan") {
                logger.info { "New enhance-plan endpoint called" }

                val request = call.receiveInBytes<PlanEnhancementRequest>()
                logger.info { "Plan enhancement request parsed, executing scheduling!" }

                schedulingQueue.execute(request) {
                    logger.info { "Plan enhancement ended, creating response." }
                    responseHandler.handle(ServerResponse(request.reportEndpoint, it))
                    logger.info { "Response sent!" }
                }

                logger.info { "Scheduling for plan enhancement enqueued!" }
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
