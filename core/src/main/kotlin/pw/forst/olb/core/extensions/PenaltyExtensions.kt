package pw.forst.olb.core.extensions

import org.optaplanner.core.api.score.buildin.hardsoftbigdecimal.HardSoftBigDecimalScore
import pw.forst.olb.core.constraints.penalty.Penalty
import pw.forst.olb.core.constraints.penalty.PenaltyBuilder

fun Penalty.toScore(): HardSoftBigDecimalScore = HardSoftBigDecimalScore.of(hard.toBigDecimal(), soft.toBigDecimal())

fun Collection<Penalty>.sum(): Penalty = with(PenaltyBuilder.create()) {
    this@sum.forEach { hard(it.hard); soft(it.soft) }
    get()
}
