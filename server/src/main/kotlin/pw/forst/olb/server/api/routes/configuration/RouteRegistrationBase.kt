package pw.forst.olb.server.api.routes.configuration

import pw.forst.olb.server.api.routes.DefaultRouteRegistrationStore


open class RouteRegistrationBase protected constructor() : RouteRegistrationStore {
    private val registrations = hashSetOf<RouteRegistration>()

    fun register(registration: RouteRegistration) {
        registrations.add(registration)
    }

    override fun obtainRegistrations(): Collection<RouteRegistration> = registrations
}

inline fun <reified T : RouteBase> DefaultRouteRegistrationStore.register() = this.register { T::class.constructors.first().call() }
