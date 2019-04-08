package pw.forst.olb.simulation.input

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import pw.forst.olb.core.extensions.prettyFormat
import pw.forst.olb.simulation.input.data.DataParser
import pw.forst.olb.simulation.input.data.JobWithHistoryFactory

class DomainBuilderTest {
    @Test
    @Disabled("Integration test!")
    fun run() {
        val builder = buildDomainBuilder()
        val plan = builder.build("/home/lukas/repos/bp/job-data/input")
        println("\n\n${plan.prettyFormat(false)}\n")
    }

    private fun buildDomainBuilder(): DomainBuilder = DomainBuilder(DataParser(), JobWithHistoryFactory())
}

