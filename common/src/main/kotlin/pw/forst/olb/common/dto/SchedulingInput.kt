package pw.forst.olb.common.dto

import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.resources.ResourcesPool

interface SchedulingInput : SchedulingProperties {
    val resources: Collection<ResourcesPool>

    val jobs: Collection<Job>
}
