package pw.forst.olb.core.constraints.penalty

object PenaltyFactory {

    private const val defaultHard: Double = 0.0

    private const val defaultSoft: Double = 0.0

    fun hardPenalty(hard: Double): Penalty = penaltyOf(hard = hard, soft = defaultSoft)

    fun softPenalty(soft: Double): Penalty = penaltyOf(hard = defaultHard, soft = soft)

    val noPenalty: Penalty = penaltyOf(hard = defaultHard, soft = defaultSoft)

    fun penaltyOf(hard: Double, soft: Double): Penalty = PenaltyImpl(hard = hard, soft = soft)
}
