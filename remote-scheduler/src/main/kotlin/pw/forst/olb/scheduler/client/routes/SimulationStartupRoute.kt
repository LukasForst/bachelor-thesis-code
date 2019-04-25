package pw.forst.olb.scheduler.client.routes

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.get
import mu.KLogging
import pw.forst.olb.common.extensions.prettyFormat
import pw.forst.olb.scheduler.client.scenarios.PeriodicConfiguration
import pw.forst.olb.scheduler.client.scenarios.RemotePlanningRound
import pw.forst.olb.server.api.routes.configuration.Route
import pw.forst.olb.server.api.routes.configuration.RouteBase
import java.util.concurrent.TimeUnit

@Route
class SimulationStartupRoute(remotePlanningRound: RemotePlanningRound, periodicConfiguration: PeriodicConfiguration) : RouteBase("simulation") {

    private companion object : KLogging()

    init {
        route {
            get("/1") {
                logger.info { "Starting simulation" }
                Thread {
                    val schedulingFunction = remotePlanningRound.buildScheduler()
                    logger.info { "\n\n${schedulingFunction().prettyFormat()}\n" }
                }.start()
                logger.info { "Simulation started!" }

                call.respond(HttpStatusCode.OK)
            }

            get("/2") {
                logger.info { "Starting simulation" }
                Thread {
                    val executor = periodicConfiguration.obtainPeriodicExecutor()
                    val finalPlan = executor
                        .startTest(
                            "./job-data/input",
                            10,
                            periodicConfiguration.generateJobs(10, TimeUnit.MINUTES.toSeconds(10)),
                            periodicConfiguration.resourcePools()
                        )

                    logger.info { "\n\n${finalPlan.prettyFormat()}\n" }
                }.start()
                logger.info { "Simulation started!" }

                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
