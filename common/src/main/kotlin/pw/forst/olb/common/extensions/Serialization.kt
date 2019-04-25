package pw.forst.olb.common.extensions

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream


fun <T : Any> serialize(data: T): ByteArray =
    with(ByteArrayOutputStream()) {
        ObjectOutputStream(this).use { it.writeObject(data) }
        toByteArray()
    }

inline fun <reified T : Any> deserialize(byteArray: Array<Byte>): T = deserialize(byteArray.toByteArray())

inline fun <reified T : Any> deserialize(byteArray: ByteArray): T = ObjectInputStream(ByteArrayInputStream(byteArray)).readObject() as T
