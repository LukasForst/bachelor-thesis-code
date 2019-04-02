package pw.forst.olb.core.constraints.penalization

import mu.KLogging
import pw.forst.olb.common.dto.impl.JobValueImpl
import pw.forst.olb.common.dto.job.CompleteJobAssignment
import pw.forst.olb.common.dto.job.Iteration
import pw.forst.olb.common.dto.job.JobValue
import pw.forst.olb.common.extensions.flattenAssignments
import pw.forst.olb.common.extensions.foldWithNext
import pw.forst.olb.core.constraints.dto.JobPlanView
import pw.forst.olb.core.constraints.penalty.Penalty
import pw.forst.olb.core.constraints.penalty.PenaltyFactory
import pw.forst.olb.core.predict.factory.CachedPrediction
import pw.forst.olb.core.predict.factory.PredictionStore

class AssignmentsEvaluation(private val predictionStore: PredictionStore) : CompletePlanEvaluation {

    private companion object : KLogging()

    private data class PredictionsEvaluations(val currentIteration: Iteration, val currentValue: JobValue, val valuesDifferencesSum: JobValue)

    override fun calculatePenalty(jobView: JobPlanView): Penalty {
        val cachedPrediction = predictionStore.predictionFor(planId = jobView.plan.uuid, jobId = jobView.job.uuid)
            ?: return PenaltyFactory.noPenalty
        val sorted = jobView.assignments
            .flattenAssignments()
            .sortedBy { it.time }

        if (sorted.isEmpty()) return PenaltyFactory.noPenalty

        val init = initPredictions(cachedPrediction, sorted.first())
        val (_, _, sum) = sorted.foldWithNext(init) { (aLastIteration, aValue, valuesSum), _, b ->
            val bIteration = cachedPrediction.getNextIteration(aLastIteration, b.allocation)
            val bValue = cachedPrediction.get(bIteration)
            PredictionsEvaluations(currentIteration = bIteration, currentValue = bValue, valuesDifferencesSum = valuesSum + (aValue - bValue))
        }

        return PenaltyFactory
            .softPenalty(sum.value)
    }

    private fun initPredictions(cachedPrediction: CachedPrediction, first: CompleteJobAssignment) = cachedPrediction.getLastIteration(first.time).let {
        PredictionsEvaluations(currentIteration = it, currentValue = cachedPrediction.get(it), valuesDifferencesSum = zeroValue())
    }


    private fun zeroValue() = JobValueImpl(0.0)
}
