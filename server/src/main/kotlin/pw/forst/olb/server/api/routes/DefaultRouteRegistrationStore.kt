package pw.forst.olb.server.api.routes

import pw.forst.olb.server.api.routes.configuration.RouteRegistrationBase
import pw.forst.olb.server.api.routes.configuration.register


class DefaultRouteRegistrationStore : RouteRegistrationBase() {
    init {
        register<HelloRoute>()
    }
}
