package pw.forst.olb.server.api.routes

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.get
import mu.KLogging
import pw.forst.olb.reporting.HelloService
import pw.forst.olb.server.api.routes.configuration.Route
import pw.forst.olb.server.api.routes.configuration.RouteBase

@Route
class HelloRoute(helloService: HelloService) : RouteBase("hello") {

    private companion object : KLogging()

    init {
        route {
            get {
                call.respond(helloService.getHello())
            }
        }

    }
}
