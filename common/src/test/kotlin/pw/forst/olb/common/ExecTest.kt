package pw.forst.olb.common

import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

class ExecTest {
    data class A(val int: Int, val uuid: UUID = UUID.randomUUID())

    @Test
    fun `copy test`() {
        val a = A(10)
        val b = a.copy(int = 2)

        assertEquals(a.uuid, b.uuid)
    }
}
