package pw.forst.olb.scheduler.client.routes

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.get
import mu.KLogging
import pw.forst.olb.common.extensions.prettyFormat
import pw.forst.olb.scheduler.client.scenarios.RemotePlanningRound
import pw.forst.olb.server.api.routes.configuration.Route
import pw.forst.olb.server.api.routes.configuration.RouteBase

@Route
class SimulationStartupRoute(remotePlanningRound: RemotePlanningRound) : RouteBase("simulation") {

    private companion object : KLogging()

    init {
        route {
            get {
                logger.info { "Starting simulation" }
                Thread {
                    val schedulingFunction = remotePlanningRound.buildScheduler()
                    logger.info { "\n\n${schedulingFunction().prettyFormat()}\n" }
                }.start()
                logger.info { "Simulation started!" }

                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
