package pw.forst.olb.core.constraints.penalization

import pw.forst.olb.common.extensions.mapToSet
import pw.forst.olb.core.constraints.dto.JobPlanView
import pw.forst.olb.core.constraints.penalty.Penalty
import pw.forst.olb.core.constraints.penalty.PenaltyBuilder

class ReallocationEvaluation : CompletePlanEvaluation {

    override fun calculatePenalty(jobView: JobPlanView): Penalty {
        val penaltyBuilder = PenaltyBuilder.create()

        val timeProviders = jobView.assignments
            .groupBy { it.time }
            .mapValues { (_, v) -> v.mapToSet { it.allocation.provider } }


        timeProviders.map { (timeKey, providers) ->
            // only one provider at time
            penaltyBuilder.hardIf(-1) { providers.size > 1 }

            val after = timeKey + jobView.plan.timeIncrement
            if (timeProviders.containsKey(after)) {
                val now = timeProviders.getValue(timeKey)
                val nextProvider = timeProviders.getValue(after)

                penaltyBuilder.hardIf(-1) { now.union(nextProvider).size > 1 }
            }
        }
        return penaltyBuilder.get()
    }
}
