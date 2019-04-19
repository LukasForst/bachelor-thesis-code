package pw.forst.olb.simulation

import pw.forst.olb.simulation.execution.ExecutionConfiguration
import pw.forst.olb.simulation.execution.OnePlanningRoundMain

class SimulationExecutor {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            OnePlanningRoundMain.main(args)
            ExecutionConfiguration.main(args)
        }
    }

}
