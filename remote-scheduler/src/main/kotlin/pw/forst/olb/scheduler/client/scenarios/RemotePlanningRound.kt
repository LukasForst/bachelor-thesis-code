package pw.forst.olb.scheduler.client.scenarios

import pw.forst.olb.core.api.OlbCoreApi
import pw.forst.olb.scheduler.client.RemoteOlbApi
import pw.forst.olb.simulation.execution.OnePlanningRoundMain

class RemotePlanningRound(private val remoteOlbApi: RemoteOlbApi) : OnePlanningRoundMain() {
    override fun buildApi(): OlbCoreApi = remoteOlbApi
}