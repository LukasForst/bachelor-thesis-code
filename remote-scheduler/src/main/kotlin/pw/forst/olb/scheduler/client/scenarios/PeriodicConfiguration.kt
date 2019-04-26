package pw.forst.olb.scheduler.client.scenarios

import pw.forst.olb.common.api.OlbCoreApi
import pw.forst.olb.scheduler.client.RemoteOlbApi
import pw.forst.olb.simulation.execution.ExecutionConfiguration

class PeriodicConfiguration(private val remoteOlbApi: RemoteOlbApi) : ExecutionConfiguration() {
    override fun buildApi(): OlbCoreApi = remoteOlbApi
}
