package pw.forst.olb.scheduler.server

import io.ktor.application.Application
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import pw.forst.olb.server.configuration.KtorSettings
import pw.forst.olb.server.configuration.setUp

fun main(arg: Array<String>) {
    embeddedServer(Netty, commandLineEnvironment(arg)).start(wait = true)
}


@Suppress("unused") // used in application.conf
fun Application.main() {
    setUp(
        KtorSettings(
            routesPackage = "pw.forst.olb.scheduler.server.routes",
            koinModules = listOf(serverModule)
        )
    )
}
