package pw.forst.olb.core.evaluation

import mu.KLogging
import org.optaplanner.core.api.score.buildin.hardsoftbigdecimal.HardSoftBigDecimalScore
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator
import pw.forst.olb.core.constraints.dto.JobPlanView
import pw.forst.olb.core.constraints.penalization.AssignmentsEvaluation
import pw.forst.olb.core.constraints.penalization.CostEvaluation
import pw.forst.olb.core.constraints.penalization.FreeSlotsEvaluation
import pw.forst.olb.core.constraints.penalization.MultipleStacksEvaluation
import pw.forst.olb.core.constraints.penalization.ReallocationEvaluation
import pw.forst.olb.core.constraints.penalization.TimeEvaluation
import pw.forst.olb.core.constraints.penalization.WrongTimeAssignedPenalization
import pw.forst.olb.core.constraints.penalty.Penalty
import pw.forst.olb.core.constraints.penalty.PenaltyFactory
import pw.forst.olb.core.domain.Plan
import pw.forst.olb.core.extensions.sum
import pw.forst.olb.core.extensions.toJobPlanViewsInPlanningWindow
import pw.forst.olb.core.extensions.toScore
import pw.forst.olb.core.predict.factory.PredictionStoreFactory

class LoggingPlanEvaluator : EasyScoreCalculator<Plan> {

    private companion object : KLogging()

    override fun calculateScore(solution: Plan): HardSoftBigDecimalScore {
        val views = solution.toJobPlanViewsInPlanningWindow()
        var penalty: Penalty = PenaltyFactory.noPenalty

        penalty += freeSlotsPenalization(solution)
        penalty += multipleStacks(views)
        penalty += timePenalty(views)
        penalty += reallocationPenalty(views)
        penalty += costPenalty(views)
        penalty += assignmentPrediction(views)
        penalty += wrongAssignments(views)

        return penalty.toScore()
    }

    private fun wrongAssignments(views: Collection<JobPlanView>): Penalty {
        val penalization = WrongTimeAssignedPenalization()
        return verifyPenalization(views, "WRONG ASSIGNMENTS") { penalization.calculatePenalty(it) }
    }

    private fun multipleStacks(views: Collection<JobPlanView>): Penalty {
        val penalization = MultipleStacksEvaluation()
        return verifyPenalization(views, "MULTIPLE STACKS") { penalization.calculatePenalty(it) }
    }


    private fun timePenalty(views: Collection<JobPlanView>): Penalty {
        val penalization = TimeEvaluation()
        return verifyPenalization(views, "TIME") { penalization.calculatePenalty(it) }
    }


    private fun reallocationPenalty(views: Collection<JobPlanView>): Penalty {
        val penalization = ReallocationEvaluation()
        return verifyPenalization(views, "REALLOCATION") { penalization.calculatePenalty(it) }
    }


    private fun costPenalty(views: Collection<JobPlanView>): Penalty {
        val penalization = CostEvaluation()
        return verifyPenalization(views, "COST") { penalization.calculatePenalty(it) }
    }

    private fun assignmentPrediction(views: Collection<JobPlanView>): Penalty {
        val penalization = AssignmentsEvaluation(PredictionStoreFactory.getStore())
        return verifyPenalization(views, "ASG PREDICT") { penalization.calculatePenalty(it) }
    }

    private fun freeSlotsPenalization(plan: Plan): Penalty {
        val penalization = FreeSlotsEvaluation()
        return penalization.calculatePenalty(plan).also {
            if (it.hard != 0.0) logger.info { "Plan - HARD penalty: FREE SLOTS - ${it.hard}" }
            if (it.soft != 0.0) logger.info { "Plan - SOFT penalty: FREE SLOTS - ${it.soft}" }
        }
    }

    private fun verifyPenalization(
        views: Collection<JobPlanView>,
        penaltyName: String,
        penalization: (JobPlanView) -> Penalty
    ): Penalty =
        views.map { view ->
            penalization(view).also {
                if (it.hard != 0.0) logger.info { "Job - ${view.job.name} - HARD penalty: $penaltyName - ${it.hard}" }
                if (it.soft != 0.0) logger.info { "Job - ${view.job.name} - SOFT penalty: $penaltyName - ${it.soft}" }
            }
        }.sum()
}
