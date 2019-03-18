package pw.forst.olb.common.dto.job

import pw.forst.olb.common.dto.resources.ResourcesAllocation
import java.util.UUID

interface Client {
    val name: String

    val uuid: UUID

    val resourcesJobRunData: Map<ResourcesAllocation, ResourcesValue>
}
