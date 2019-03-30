package pw.forst.olb.core.evaluation

import mu.KLogging
import org.optaplanner.core.api.score.buildin.hardsoftbigdecimal.HardSoftBigDecimalScore
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator
import pw.forst.olb.core.constraints.penalization.CompletePlanPenalization
import pw.forst.olb.core.constraints.penalization.CostPenalization
import pw.forst.olb.core.constraints.penalization.NoAssignmentPenalization
import pw.forst.olb.core.constraints.penalization.ReallocationPenalization
import pw.forst.olb.core.constraints.penalization.TimePenalization
import pw.forst.olb.core.domain.Plan
import pw.forst.olb.core.extensions.sum
import pw.forst.olb.core.extensions.toJobPlanViews
import pw.forst.olb.core.extensions.toScore
import java.math.BigDecimal

class BigDecimalScoreCalculation : EasyScoreCalculator<Plan> {

    private companion object : KLogging()

    private val penalties: Collection<CompletePlanPenalization> = listOf(
        CostPenalization(),
        TimePenalization(),
        ReallocationPenalization(),
//        MultipleStacksPenalization(),
        NoAssignmentPenalization()
    )

    override fun calculateScore(solution: Plan?): HardSoftBigDecimalScore {
        if (solution == null) return HardSoftBigDecimalScore.of(BigDecimal(Long.MAX_VALUE), BigDecimal(Long.MAX_VALUE)).also { logger.warn { "Null received!" } }

        return solution.toJobPlanViews()
            .flatMap { planView -> penalties.map { it.calculatePenalty(planView) } }
            .sum()
            .toScore()
    }


}
