package pw.forst.olb.common.dto

import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.resources.ResourcesAllocation
import java.io.Serializable

interface JobResourcesAllocation : Serializable {

    val job: Job

    val allocation: ResourcesAllocation
}
