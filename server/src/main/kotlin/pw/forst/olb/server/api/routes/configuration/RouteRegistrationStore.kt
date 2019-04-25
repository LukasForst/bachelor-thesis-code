package pw.forst.olb.server.api.routes.configuration

interface RouteRegistrationStore {
    fun obtainRegistrations(): Collection<RouteRegistrationLambda>
}
