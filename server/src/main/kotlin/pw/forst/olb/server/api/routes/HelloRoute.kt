package pw.forst.olb.server.api.routes

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.get
import mu.KLogging
import org.koin.standalone.inject
import pw.forst.olb.reporting.HelloService
import pw.forst.olb.server.api.routes.configuration.RouteBase

class HelloRoute : RouteBase("hello") {

    private companion object : KLogging()

    private val helloService: HelloService by inject()

    init {
        route {
            get {
                call.respond(helloService.getHello())
            }
        }

    }
}
