package pw.forst.olb.server.extensions

import com.google.gson.GsonBuilder
import pw.forst.olb.server.configuration.interfaceSerializer

inline fun <reified T : Any, reified R : Any> GsonBuilder.registerInterfaceTypeAdapter() {
    registerTypeAdapter(T::class.java, interfaceSerializer<R>())
}
