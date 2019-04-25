package pw.forst.olb.server.configuration

import io.ktor.client.HttpClient
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import org.koin.dsl.module.module

val serverModule = module {
    // http client
    factory {
        HttpClient {
            install(JsonFeature) {
                serializer = GsonSerializer {
                    setPrettyPrinting()
                    enableComplexMapKeySerialization()
                    serializeNulls()
                    serializeSpecialFloatingPointValues()
                    generateNonExecutableJson()
                    setLenient()
                }
            }
        }
    }
}
