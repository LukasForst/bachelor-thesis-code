package pw.forst.olb.scheduler.client.scenarios

import pw.forst.olb.common.api.OlbCoreApi
import pw.forst.olb.scheduler.client.RemoteOlbApi
import pw.forst.olb.simulation.execution.OnePlanningRoundSimulation

class RemotePlanningRound(private val remoteOlbApi: RemoteOlbApi) : OnePlanningRoundSimulation() {
    override fun buildApi(): OlbCoreApi = remoteOlbApi
}
