package pw.forst.olb.core.constraints.dto

import pw.forst.olb.common.dto.GenericPlan
import pw.forst.olb.common.dto.job.CompleteJobAssignment
import pw.forst.olb.common.dto.job.Job

data class JobPlanViewImpl(
    override val job: Job,
    override val assignments: Collection<CompleteJobAssignment>,
    override val plan: GenericPlan
) : JobPlanView
