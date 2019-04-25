package pw.forst.olb.server.configuration

import io.ktor.application.Application
import org.koin.dsl.module.module
import pw.forst.olb.server.api.routes.configuration.RouteRegistrationProvider

fun Application.resolveDependencies(ktorSettings: KtorSettings) = ApplicationDependencyBuilder()
    .registerModule(module { single { this@resolveDependencies } }) // add application registration, if needed
    .registerModules(ktorSettings.koinModules)
    .buildKoinModules()
    .routesBy { RouteRegistrationProvider.getStore(ktorSettings.routesPackage) }
    .configure()

fun Application.setUp(ktorSettings: KtorSettings) {
    resolveDependencies(ktorSettings)
    configureInstallation()
}
