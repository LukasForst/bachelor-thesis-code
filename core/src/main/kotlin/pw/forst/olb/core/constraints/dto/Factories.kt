package pw.forst.olb.core.constraints.dto

import pw.forst.olb.common.dto.job.CompleteJobAssignment
import pw.forst.olb.common.dto.job.Job

fun jobPlanView(job: Job, assignments: Collection<CompleteJobAssignment>) = JobPlanViewImpl(job, assignments)
