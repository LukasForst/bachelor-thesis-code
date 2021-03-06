package pw.forst.olb.scheduler.server

import io.ktor.application.Application
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import pw.forst.olb.common.config.SCHEDULING_SERVER_PORT
import pw.forst.olb.server.configuration.KtorSettings
import pw.forst.olb.server.configuration.setUp

fun main(arg: Array<String>) {
    val env = applicationEngineEnvironment {
        module {
            main()
        }

        connector {
            port = SCHEDULING_SERVER_PORT
        }
    }

    embeddedServer(Netty, env).start(true)
}


fun Application.main() {
    setUp(
        KtorSettings(
            routesPackage = "pw.forst.olb.scheduler.server.routes",
            koinModules = listOf(serverModule)
        )
    )
}
