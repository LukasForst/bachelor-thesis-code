package pw.forst.olb.core.extensions

import pw.forst.olb.common.dto.GenericPlan
import pw.forst.olb.common.dto.job.JobAssignment

fun JobAssignment.isInPlanningWindow(plan: GenericPlan): Boolean = plan.startTime <= this.time && this.time <= plan.endTime
