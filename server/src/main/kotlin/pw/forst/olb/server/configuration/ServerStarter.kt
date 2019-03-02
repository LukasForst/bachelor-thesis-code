package pw.forst.olb.server.configuration

import io.ktor.application.Application
import org.koin.dsl.module.module
import pw.forst.olb.reporting.configuration.reportingModule
import pw.forst.olb.server.api.routes.configuration.RouteRegistrationProvider

fun Application.resolveDependencies() = ApplicationDependencyBuilder()
    .registerModule(module { single { this@resolveDependencies } }) // add application registration, if needed
    .registerModule(reportingModule)
    .registerModule(serverModule)
    .buildKoinModules()
    .routesBy { RouteRegistrationProvider.getStore(this) }
    .configure()
