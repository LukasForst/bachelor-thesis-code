package pw.forst.olb.core.constraints

import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.resources.ResourcesAllocation
import pw.forst.olb.common.dto.scheduling.JobPlanView

class CostConstraint : JobAssignmentConstraint {

    override fun canAssign(jobView: JobPlanView, allocation: ResourcesAllocation, time: Time): Boolean = jobView.totalCost + allocation.cost <= jobView.job.parameters.maxCost

}
