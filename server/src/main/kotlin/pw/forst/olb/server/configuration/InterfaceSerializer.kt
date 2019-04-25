package pw.forst.olb.server.configuration

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type


class InterfaceSerializer<T : Any>(private val implementationClass: Class<T>) : JsonSerializer<T>, JsonDeserializer<T> {

    override fun serialize(value: T?, type: Type, context: JsonSerializationContext): JsonElement {
        val targetType = value?.javaClass ?: type
        return context.serialize(value, targetType)
    }

    override fun deserialize(jsonElement: JsonElement, typeOfT: Type, context: JsonDeserializationContext): T {
        return context.deserialize(jsonElement, implementationClass)
    }
}

inline fun <reified T : Any> interfaceSerializer(): InterfaceSerializer<T> = InterfaceSerializer(T::class.java)
