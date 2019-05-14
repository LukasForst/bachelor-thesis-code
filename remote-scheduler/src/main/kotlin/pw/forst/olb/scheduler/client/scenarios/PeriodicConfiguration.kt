package pw.forst.olb.scheduler.client.scenarios

import pw.forst.olb.common.api.OlbCoreApi
import pw.forst.olb.scheduler.client.RemoteOlbApi
import pw.forst.olb.simulation.execution.RuntimeSimulation

class PeriodicConfiguration(private val remoteOlbApi: RemoteOlbApi) : RuntimeSimulation() {
    override fun buildApi(): OlbCoreApi = remoteOlbApi
}
