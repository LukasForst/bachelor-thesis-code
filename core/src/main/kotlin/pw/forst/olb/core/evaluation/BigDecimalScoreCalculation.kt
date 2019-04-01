package pw.forst.olb.core.evaluation

import mu.KLogging
import org.optaplanner.core.api.score.buildin.hardsoftbigdecimal.HardSoftBigDecimalScore
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator
import pw.forst.olb.core.constraints.penalization.AssignmentsEvaluation
import pw.forst.olb.core.constraints.penalization.CompletePlanEvaluation
import pw.forst.olb.core.constraints.penalization.CostEvaluation
import pw.forst.olb.core.constraints.penalization.NoAssignmentEvaluation
import pw.forst.olb.core.constraints.penalization.ReallocationEvaluation
import pw.forst.olb.core.constraints.penalization.TimeEvaluation
import pw.forst.olb.core.domain.Plan
import pw.forst.olb.core.extensions.sum
import pw.forst.olb.core.extensions.toJobPlanViews
import pw.forst.olb.core.extensions.toScore
import pw.forst.olb.core.predict.factory.PredictionStoreFactory

class BigDecimalScoreCalculation : EasyScoreCalculator<Plan> {

    private companion object : KLogging()

    private val penalties: Collection<CompletePlanEvaluation> = listOf(
        CostEvaluation(),
        TimeEvaluation(),
        ReallocationEvaluation(),
        NoAssignmentEvaluation(),
        AssignmentsEvaluation(PredictionStoreFactory.getStore())
    )

    override fun calculateScore(solution: Plan): HardSoftBigDecimalScore {
        return solution.toJobPlanViews()
            .flatMap { planView -> penalties.map { it.calculatePenalty(planView) } }
            .sum()
            .toScore()
    }


}
