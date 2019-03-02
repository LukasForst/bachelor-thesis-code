package pw.forst.olb.server

import io.ktor.application.Application
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import pw.forst.olb.server.configuration.resolveDependencies
import pw.forst.olb.server.configuration.configureInstallation

fun main(arg: Array<String>) {
    embeddedServer(Netty, commandLineEnvironment(arg)).start(wait = true)
}


@Suppress("unused") // used in application.conf
fun Application.main() {

    resolveDependencies()

    configureInstallation()

}
