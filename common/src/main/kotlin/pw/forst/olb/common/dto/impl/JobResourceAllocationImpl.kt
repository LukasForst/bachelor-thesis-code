package pw.forst.olb.common.dto.impl

import pw.forst.olb.common.dto.JobResourcesAllocation
import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.resources.ResourcesAllocation

data class JobResourceAllocationImpl(
    override val job: Job,
    override val allocation: ResourcesAllocation
) : JobResourcesAllocation
