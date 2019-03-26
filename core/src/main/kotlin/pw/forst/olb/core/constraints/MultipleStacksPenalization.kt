package pw.forst.olb.core.constraints

import pw.forst.olb.common.extensions.mapToSet
import pw.forst.olb.core.constraints.dto.JobPlanView
import pw.forst.olb.core.constraints.penalty.Penalty
import pw.forst.olb.core.constraints.penalty.PenaltyBuilder

class MultipleStacksPenalization : PlanPenalization {

    override fun calculatePenalty(jobView: JobPlanView): Penalty = jobView.assignments
        .groupBy { it.time }
        .mapValues { (_, value) -> value.mapToSet { it.allocation.provider }.size }
        .map { (_, allocationProviderCount) -> 1 - allocationProviderCount }
        .filter { it < 0 }
        .sum()
        .let { PenaltyBuilder.create().hard(it).get() }
}
