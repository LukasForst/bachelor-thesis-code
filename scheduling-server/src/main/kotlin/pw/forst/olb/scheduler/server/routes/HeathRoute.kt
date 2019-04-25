package pw.forst.olb.scheduler.server.routes

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.get
import pw.forst.olb.server.api.routes.configuration.Route
import pw.forst.olb.server.api.routes.configuration.RouteBase

@Route
class HeathRoute : RouteBase("health") {
    init {
        route {
            get {
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
