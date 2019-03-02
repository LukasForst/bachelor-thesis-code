package pw.forst.olb.server.api.routes.configuration

import mu.KLogging
import org.koin.standalone.KoinComponent
import org.koin.standalone.get
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure


open class RouteRegistrationBase protected constructor() : RouteRegistrationStore, KoinComponent {

    private companion object : KLogging()

    private val registrations = hashSetOf<RouteRegistration>()

    fun <T : RouteBase> register(clazz: KClass<T>) {
        registrations.add {
            with(clazz.primaryConstructor!!) {
                callBy(parameters.associateWith { get<Any>(clazz = it.type.jvmErasure) })
            }
        }

    }

    override fun obtainRegistrations(): Collection<RouteRegistration> = registrations
}

inline fun <reified T : RouteBase> RouteRegistrationBase.register() = this.register(T::class)
