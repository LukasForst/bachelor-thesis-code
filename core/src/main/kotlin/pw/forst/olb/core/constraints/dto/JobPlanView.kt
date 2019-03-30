package pw.forst.olb.core.constraints.dto

import pw.forst.olb.common.dto.job.CompleteJobAssignment
import pw.forst.olb.common.dto.job.Job

interface JobPlanView {
    val job: Job

    val assignments: Collection<CompleteJobAssignment>
}
