package pw.forst.olb.core.opta

import mu.KLogging
import org.optaplanner.core.api.score.Score
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore
import org.optaplanner.core.api.score.buildin.simple.SimpleScore
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator
import pw.forst.olb.core.opta.domain.Plan
import pw.forst.olb.core.opta.domain.getTimeLengthForJob

class EasyScoreCalculation : EasyScoreCalculator<Plan> {
    private companion object : KLogging()

    override fun calculateScore(solution: Plan?): Score<out Score<*>> {
        if (solution == null) return HardSoftScore.of(Int.MAX_VALUE, Int.MAX_VALUE).also { logger.warn { "Null received!" } }
        var score = 0

        for (job in solution.jobDomain) {
            if (solution.assignments.none { it.job == job }) score += -1

            if (solution.getTimeLengthForJob(job) > job.maxTime) score += -1
        }

        for (time in solution.times) {
            val all = solution.assignments
                .filter { it.time == time }
                .mapNotNull { it.job }
            if (all.size != all.toSet().size) score += -1
        }

        return SimpleScore.of(score)
    }

}
