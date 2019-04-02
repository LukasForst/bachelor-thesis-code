package pw.forst.olb.common.dto

import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.resources.ResourcesAllocation

interface JobResourcesAllocation {

    val job: Job

    val allocation: ResourcesAllocation
}
