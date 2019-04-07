package pw.forst.olb.core.evaluation

import mu.KLogging
import org.optaplanner.core.api.score.buildin.hardsoftbigdecimal.HardSoftBigDecimalScore
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator
import pw.forst.olb.core.constraints.penalization.AssignmentsEvaluation
import pw.forst.olb.core.constraints.penalization.CompletePlanEvaluation
import pw.forst.olb.core.constraints.penalization.CostEvaluation
import pw.forst.olb.core.constraints.penalization.FreeSlotsEvaluation
import pw.forst.olb.core.constraints.penalization.PlanEvaluation
import pw.forst.olb.core.constraints.penalization.ReallocationEvaluation
import pw.forst.olb.core.constraints.penalization.TimeEvaluation
import pw.forst.olb.core.domain.Plan
import pw.forst.olb.core.extensions.sum
import pw.forst.olb.core.extensions.toJobPlanViewsInPlanningWindow
import pw.forst.olb.core.extensions.toScore
import pw.forst.olb.core.predict.factory.PredictionStoreFactory

class BigDecimalScoreCalculation : EasyScoreCalculator<Plan> {

    private companion object : KLogging()

    private val jobPenalties: Collection<CompletePlanEvaluation> = listOf(
        CostEvaluation(),
        TimeEvaluation(),
        ReallocationEvaluation(), // this shouldn't be necessary since there is a moves that prohibits it
//        NoAssignmentEvaluation(),
        AssignmentsEvaluation(PredictionStoreFactory.getStore())
    )

    private val planPenalties: Collection<PlanEvaluation> = listOf(
        FreeSlotsEvaluation()
    )

    override fun calculateScore(solution: Plan): HardSoftBigDecimalScore {
        val planPenalties = planPenalties.map { it.calculatePenalty(solution) }.sum()

        val jobPenalties = solution.toJobPlanViewsInPlanningWindow()
            .flatMap { planView -> jobPenalties.map { it.calculatePenalty(planView) } }
            .sum()

        return (planPenalties + jobPenalties).toScore()
    }


}
