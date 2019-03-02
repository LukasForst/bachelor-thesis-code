package pw.forst.olb.server.api.routes.configuration

import pw.forst.olb.server.api.routes.HelloRoute


class DefaultRouteRegistrationStore : RouteRegistrationBase() {
    init {
        register<HelloRoute>()
    }
}
