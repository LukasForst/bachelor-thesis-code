package pw.forst.olb.common.dto

import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.common.dto.resources.ResourcesPool
import java.io.Serializable

interface SchedulingInput : SchedulingProperties, Serializable {
    val resources: Collection<ResourcesPool>

    val jobs: Collection<Job>
}
