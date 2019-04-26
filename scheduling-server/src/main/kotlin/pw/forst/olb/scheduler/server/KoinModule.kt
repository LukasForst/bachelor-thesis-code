package pw.forst.olb.scheduler.server

import com.google.gson.GsonBuilder
import io.ktor.client.HttpClient
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import org.koin.dsl.module.module
import pw.forst.olb.common.api.OlbCoreApi
import pw.forst.olb.core.api.InputToDomainConverter
import pw.forst.olb.core.api.OlbCoreApiImpl
import pw.forst.olb.core.solver.OptaplannerSolverFactory
import java.util.concurrent.Executors

val serverModule = module {
    // Gson
    factory {
        GsonBuilder().setUp().create()
    }

    // http client
    factory(override = true) {
        HttpClient {
            install(JsonFeature) {
                serializer = GsonSerializer { setUp() }
            }
        }
    }

    // scheduling core
    single { InputToDomainConverter() }
    single { OptaplannerSolverFactory() }
    single<OlbCoreApi> { OlbCoreApiImpl(get(), get(), true) }

    // response handler, need http client
    single { ResponseHandler(get()) }

    // scheduling queue, needs core API and executor service
    single { SchedulingQueue(get(), Executors.newSingleThreadScheduledExecutor()) }
}

private fun GsonBuilder.setUp(): GsonBuilder {
    setPrettyPrinting()
    enableComplexMapKeySerialization()
    serializeNulls()
    serializeSpecialFloatingPointValues()
    generateNonExecutableJson()
    setLenient()
    return this
}
