package pw.forst.olb.scheduler.client.routes

import io.ktor.routing.get
import mu.KLogging
import pw.forst.olb.common.extensions.prettyFormat
import pw.forst.olb.scheduler.client.RemotePlanningRound
import pw.forst.olb.server.api.routes.configuration.Route
import pw.forst.olb.server.api.routes.configuration.RouteBase

@Route
class SimulationStartupRoute(remotePlanningRound: RemotePlanningRound) : RouteBase("simulation") {

    private companion object : KLogging()

    init {
        route {
            get {
                logger.info { "Starting simulation" }
                val schedulingFunction = remotePlanningRound.buildScheduler()
                println("\n\n${schedulingFunction().prettyFormat()}\n")
            }
        }
    }
}
