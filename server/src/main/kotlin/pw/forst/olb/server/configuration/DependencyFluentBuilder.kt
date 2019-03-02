package pw.forst.olb.server.configuration

import org.koin.dsl.module.Module
import org.koin.standalone.StandAloneContext.startKoin
import pw.forst.olb.server.api.routes.configuration.RouteRegistrationLambda
import pw.forst.olb.server.api.routes.configuration.RouteRegistrationStore

/**
 * Register Koin modules [org.koin.dsl.module.Module]
 * */
interface ModuleRegistryEmpty {
    fun registerModule(module: Module): FromModuleToServiceStep

    fun registerModuleBy(module: () -> Module): FromModuleToServiceStep
}

/**
 * Execute [org.koin.standalone.StandAloneContext.startKoin] and go to next registration phase
 * */
interface FromModuleToServiceStep : ModuleRegistryEmpty {
    fun buildKoinModules(): RoutesStep
}

/**
 * Provide [RouteRegistrationStore]
 * */
interface RoutesStep {

    fun routes(registryProvider: RouteRegistrationStore): RoutesStep

    fun routesBy(registryProvider: () -> RouteRegistrationStore): RoutesStep

    fun configure()
}

/**
 * Fluent builder implementation
 * */
class ApplicationDependencyBuilder : ModuleRegistryEmpty, FromModuleToServiceStep, RoutesStep {

    private val modules = arrayListOf<Module>()
    private val routes = arrayListOf<RouteRegistrationLambda>()

    override fun registerModule(module: Module): FromModuleToServiceStep = modules.add(module).run { this@ApplicationDependencyBuilder }

    override fun registerModuleBy(module: () -> Module): FromModuleToServiceStep = modules.add(module.invoke()).run { this@ApplicationDependencyBuilder }


    override fun buildKoinModules(): RoutesStep = startKoin(modules).run { this@ApplicationDependencyBuilder }

    override fun routes(registryProvider: RouteRegistrationStore): RoutesStep = routes.addAll(registryProvider.obtainRegistrations()).run { this@ApplicationDependencyBuilder }

    override fun routesBy(registryProvider: () -> RouteRegistrationStore): RoutesStep =
        routes.addAll(registryProvider.invoke().obtainRegistrations()).run { this@ApplicationDependencyBuilder }

    override fun configure() = routes.forEach { it.invoke() }
}
