package pw.forst.olb.server.api.routes.configuration

import org.reflections.Reflections


class ReflexiveRouteRegistrationStore(routesPackage: String) : RouteRegistrationBase() {
    init {
        Reflections(routesPackage)
            .getTypesAnnotatedWith(Route::class.java)
            .forEach { register(it.kotlin) }
    }
}
