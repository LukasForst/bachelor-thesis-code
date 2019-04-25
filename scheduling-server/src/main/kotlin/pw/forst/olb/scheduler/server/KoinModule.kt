package pw.forst.olb.scheduler.server

import io.ktor.client.HttpClient
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import org.koin.dsl.module.module
import pw.forst.olb.core.api.InputToDomainConverter
import pw.forst.olb.core.api.OlbCoreApi
import pw.forst.olb.core.api.OlbCoreApiImpl
import pw.forst.olb.core.solver.OptaplannerSolverFactory
import java.util.concurrent.Executors

val serverModule = module {
    // http client
    factory {
        HttpClient {
            install(JsonFeature) {
                serializer = GsonSerializer {
                    serializeNulls()
                    disableHtmlEscaping()
                    setPrettyPrinting()
                }
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
