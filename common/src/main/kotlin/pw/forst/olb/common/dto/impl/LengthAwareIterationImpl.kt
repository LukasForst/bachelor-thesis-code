package pw.forst.olb.common.dto.impl

import pw.forst.olb.common.dto.job.Iteration
import pw.forst.olb.common.dto.job.LengthAwareIteration

data class LengthAwareIterationImpl(
    override val position: Int,
    override val iterationLengthInMls: Int
) : LengthAwareIteration {
    override fun plus(other: Iteration): Iteration = copy(position = this.position + other.position)
}

data class IterationImpl(override val position: Int) : Iteration {
    override fun plus(other: Iteration): Iteration = copy(position = this.position + other.position)
}

fun createIteration(position: Int, iterationLength: Int): Iteration = LengthAwareIterationImpl(position, iterationLength)

fun createIteration(position: Int): Iteration = IterationImpl(position)
