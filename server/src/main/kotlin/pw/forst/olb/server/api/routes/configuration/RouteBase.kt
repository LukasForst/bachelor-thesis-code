package pw.forst.olb.server.api.routes.configuration

import io.ktor.application.Application
import io.ktor.routing.Route
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.util.pipeline.ContextDsl
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

abstract class RouteBase(private val contextPrefix: String? = null) : KoinComponent {

    private companion object {
        const val globalPrefix = "v1"
    }

    private val app by inject<Application>()

    @ContextDsl
    protected fun route(endpoint: String? = null, build: Route.() -> Unit): Route =
        app.routing {
            route(buildPath(endpoint)) {
                build()
            }
        }

    private fun buildPath(endpoint: String?) = globalPrefix + if (contextPrefix != null) "/$contextPrefix" else "" + if (endpoint != null) "/$endpoint" else ""
}
