package pw.forst.olb.core.constraints.penalty

class PenaltyBuilder private constructor() {

    companion object {
        fun create(): PenaltyBuilder = PenaltyBuilder()
    }

    private var hard: Double = 0.0
    private var soft: Double = 0.0

    fun hard(value: Double): PenaltyBuilder = this.also { hard += value }

    fun hard(value: Int): PenaltyBuilder = this.also { hard += value }

    fun hardIf(value: Double, check: () -> Boolean) = this.also { if (check()) hard(value) }

    fun hardIf(value: Int, check: () -> Boolean) = this.also { if (check()) hard(value) }

    fun soft(value: Double): PenaltyBuilder = this.also { soft += value }

    fun soft(value: Int): PenaltyBuilder = this.also { soft += value }

    fun softIf(value: Double, check: () -> Boolean) = this.also { if (check()) soft(value) }

    fun softIf(value: Int, check: () -> Boolean) = this.also { if (check()) soft(value) }

    fun get(): Penalty = PenaltyFactory.penaltyOf(hard = hard, soft = soft)

}
