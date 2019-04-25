package pw.forst.olb.server.extensions

import io.ktor.application.ApplicationCall
import io.ktor.request.receive
import pw.forst.olb.common.extensions.deserialize

suspend inline fun <reified T : Any> ApplicationCall.receiveInBytes(): T = deserialize(this.receive<ByteArray>())
