package pw.forst.olb.simulation.input

import org.junit.jupiter.api.Test
import pw.forst.olb.common.dto.TimeImpl
import pw.forst.olb.common.dto.impl.SchedulingPropertiesImpl
import pw.forst.olb.core.api.InputToDomainConverter
import pw.forst.olb.core.api.OlbCoreApiImpl
import pw.forst.olb.core.extensions.prettyFormat
import pw.forst.olb.core.solver.OptaplannerSolverFactory
import pw.forst.olb.simulation.input.data.DataParser
import pw.forst.olb.simulation.input.data.JobWithHistoryFactory
import java.util.concurrent.TimeUnit

class DomainBuilderTest {
    @Test
//    @Disabled("Integration test!")
    fun run() {
        val builder = buildDomainBuilder()
        val plan = builder.build("/home/lukas/repos/bp/job-data/input")

        val result = OlbCoreApiImpl(InputToDomainConverter(), OptaplannerSolverFactory(), true).enhancePlan(
            plan, SchedulingPropertiesImpl(
                startTime = plan.startTime + (plan.timeIncrement * 3),
                endTime = plan.endTime,
                timeStep = plan.timeIncrement,
                maxTimePlanningSpend = TimeImpl(30, TimeUnit.SECONDS),
                cores = 3
            )
        )

        println("\n\n${result.prettyFormat(false)}\n")
    }

    private fun buildDomainBuilder(): DomainBuilder = DomainBuilder(DataParser(), JobWithHistoryFactory())
}

