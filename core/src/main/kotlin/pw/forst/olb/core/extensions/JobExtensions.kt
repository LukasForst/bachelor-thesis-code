package pw.forst.olb.core.extensions

import pw.forst.olb.common.dto.job.Job
import pw.forst.olb.core.domain.PlanningJob

fun Job.asSchedulingEntity(): PlanningJob = if (this is PlanningJob) this else PlanningJob(parameters, client, name, uuid)
