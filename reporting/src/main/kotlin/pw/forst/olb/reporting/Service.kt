package pw.forst.olb.reporting

import kotlinx.coroutines.delay

class HelloService {
    suspend fun getHello() = delay(10).let { "hello world!" }
}
