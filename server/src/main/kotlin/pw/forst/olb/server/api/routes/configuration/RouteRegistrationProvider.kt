package pw.forst.olb.server.api.routes.configuration

import io.ktor.application.Application

object RouteRegistrationProvider {

    @Suppress("UNUSED_PARAMETER") //in the future it is possible to determine which registration store should be used based on app configuration
    fun getStore(app: Application): RouteRegistrationStore = ReflexiveRouteRegistrationStore()
}
