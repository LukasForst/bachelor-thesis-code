package pw.forst.olb.server.configuration

import org.koin.dsl.module.Module

data class KtorSettings(
    val routesPackage: String,
    val koinModules: Collection<Module>
)
