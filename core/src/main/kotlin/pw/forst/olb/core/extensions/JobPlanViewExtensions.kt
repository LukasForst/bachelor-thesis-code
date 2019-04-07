package pw.forst.olb.core.extensions

import pw.forst.olb.core.constraints.dto.JobPlanView
import pw.forst.olb.core.constraints.dto.JobPlanViewImpl

fun JobPlanView.filterInPlanningWindows(): JobPlanView = JobPlanViewImpl(
    job,
    this.assignments.filter { it.isInPlanningWindow(this.plan) },
    this.plan
)
