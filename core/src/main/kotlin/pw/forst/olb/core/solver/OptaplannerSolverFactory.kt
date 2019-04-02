package pw.forst.olb.core.solver

import mu.KLogging
import org.optaplanner.core.api.solver.Solver
import org.optaplanner.core.api.solver.SolverFactory
import pw.forst.olb.common.dto.SolverConfiguration

class OptaplannerSolverFactory {

    private companion object : KLogging() {
        const val DEFAULT_CONFIG_XML = "solverConfiguration.xml"
    }

    fun <T> create(configuration: SolverConfiguration): Solver<T> {
        return SolverFactory.createFromXmlResource<T>(DEFAULT_CONFIG_XML).apply {
            //            solverConfig.moveThreadCount = configuration.threads.toString()
            solverConfig.terminationConfig.secondsSpentLimit = configuration.maxTimeInSeconds
        }.buildSolver()
    }
}
