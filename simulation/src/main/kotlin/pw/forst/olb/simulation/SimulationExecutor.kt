package pw.forst.olb.simulation

import pw.forst.olb.simulation.execution.OnePlanningRoundSimulation
import pw.forst.olb.simulation.execution.RuntimeSimulation

class SimulationExecutor {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            OnePlanningRoundSimulation.main(args) // simulation #1
            RuntimeSimulation.main(args) // simulation #2
        }
    }

}
