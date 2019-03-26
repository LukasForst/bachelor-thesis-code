package pw.forst.olb.core.evaluation

import mu.KLogging
import org.optaplanner.core.api.score.buildin.hardsoftbigdecimal.HardSoftBigDecimalScore
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator
import pw.forst.olb.core.constraints.CostPenalization
import pw.forst.olb.core.constraints.MultipleStacksPenalization
import pw.forst.olb.core.constraints.NoAssignmentPenalty
import pw.forst.olb.core.constraints.PlanPenalization
import pw.forst.olb.core.constraints.ReallocationPenalization
import pw.forst.olb.core.constraints.TimePenalization
import pw.forst.olb.core.domain.Plan
import pw.forst.olb.core.extensions.sum
import pw.forst.olb.core.extensions.toJobPlanViews
import pw.forst.olb.core.extensions.toScore
import java.math.BigDecimal

class BigDecimalScoreCalculation : EasyScoreCalculator<Plan> {

    private companion object : KLogging()

    private val penalties: Collection<PlanPenalization> = listOf(
        CostPenalization(),
        TimePenalization(),
        ReallocationPenalization(),
        MultipleStacksPenalization(),
        NoAssignmentPenalty()
    )

    override fun calculateScore(solution: Plan?): HardSoftBigDecimalScore {
        if (solution == null) return HardSoftBigDecimalScore.of(BigDecimal(Long.MAX_VALUE), BigDecimal(Long.MAX_VALUE)).also { logger.warn { "Null received!" } }

        return solution.toJobPlanViews()
            .flatMap { planView -> penalties.map { it.calculatePenalty(planView) } }
            .sum()
            .toScore()
    }


}
