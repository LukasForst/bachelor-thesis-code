package pw.forst.olb.server.api.routes.configuration

import org.reflections.Reflections


class ReflexiveRouteRegistrationStore : RouteRegistrationBase() {
    init {
        Reflections("pw.forst.olb.server.api.routes")
            .getTypesAnnotatedWith(Route::class.java)
            .forEach { register(it.kotlin) }
    }
}
