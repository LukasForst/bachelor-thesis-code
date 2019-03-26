package pw.forst.olb.core.evaluation

import mu.KLogging
import org.optaplanner.core.api.score.buildin.hardsoftbigdecimal.HardSoftBigDecimalScore
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator
import pw.forst.olb.core.constraints.CostPenalization
import pw.forst.olb.core.constraints.MultipleStacksPenalization
import pw.forst.olb.core.constraints.ReallocationPenalization
import pw.forst.olb.core.constraints.TimePenalization
import pw.forst.olb.core.constraints.dto.JobPlanView
import pw.forst.olb.core.constraints.penalty.Penalty
import pw.forst.olb.core.constraints.penalty.PenaltyFactory
import pw.forst.olb.core.domain.Plan
import pw.forst.olb.core.extensions.sum
import pw.forst.olb.core.extensions.toJobPlanViews
import pw.forst.olb.core.extensions.toScore
import java.math.BigDecimal

class LoggingPlanEvaluator : EasyScoreCalculator<Plan> {

    private companion object : KLogging()

    override fun calculateScore(solution: Plan?): HardSoftBigDecimalScore {
        if (solution == null) return HardSoftBigDecimalScore.of(BigDecimal(Long.MAX_VALUE), BigDecimal(Long.MAX_VALUE)).also { logger.warn { "Null received!" } }
        val views = solution.toJobPlanViews()
        var penalty: Penalty = PenaltyFactory.noPenalty

        penalty += multipleStacks(views)
        penalty += timePenalty(views)
        penalty += reallocationPenalty(views)
        penalty += costPenalty(views)

        return penalty.toScore()
    }

    private fun multipleStacks(views: Collection<JobPlanView>): Penalty {
        val penalization = MultipleStacksPenalization()
        return verifyPenalization(views, "MULTIPLE STACKS") { penalization.calculatePenalty(it) }
    }


    private fun timePenalty(views: Collection<JobPlanView>): Penalty {
        val penalization = TimePenalization()
        return verifyPenalization(views, "TIME") { penalization.calculatePenalty(it) }
    }


    private fun reallocationPenalty(views: Collection<JobPlanView>): Penalty {
        val penalization = ReallocationPenalization()
        return verifyPenalization(views, "REALLOCATION") { penalization.calculatePenalty(it) }
    }


    private fun costPenalty(views: Collection<JobPlanView>): Penalty {
        val penalization = CostPenalization()
        return verifyPenalization(views, "COST") { penalization.calculatePenalty(it) }
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
