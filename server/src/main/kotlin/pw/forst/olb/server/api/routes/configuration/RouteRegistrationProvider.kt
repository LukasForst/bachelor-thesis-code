package pw.forst.olb.server.api.routes.configuration

object RouteRegistrationProvider {

    fun getStore(routesPackage: String): RouteRegistrationStore = ReflexiveRouteRegistrationStore(routesPackage)
}
