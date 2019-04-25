package pw.forst.olb.scheduler.client.scenarios

import pw.forst.olb.core.api.OlbCoreApi
import pw.forst.olb.scheduler.client.RemoteOlbApi
import pw.forst.olb.simulation.execution.ExecutionConfiguration

class PeriodicConfiguration(private val remoteOlbApi: RemoteOlbApi) : ExecutionConfiguration() {
    override fun buildApi(): OlbCoreApi = remoteOlbApi
}
