package pw.forst.olb.core.constraints

import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.resources.ResourcesAllocation
import pw.forst.olb.common.dto.scheduling.JobPlanView

class ReallocationConstraint : JobAssignmentConstraint {

    override fun canAssign(jobView: JobPlanView, allocation: ResourcesAllocation, time: Time): Boolean {
        val currentAllocation = jobView.assignments[time - jobView.timeStep] ?: return true
        return currentAllocation.allocation.provider == allocation.provider
    }
}
