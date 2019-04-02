package pw.forst.olb.common.dto.impl

import pw.forst.olb.common.dto.job.Iteration
import pw.forst.olb.common.dto.job.LengthAwareIteration

data class LengthAwareIteration(
    override val position: Long,
    override val iterationLengthInMls: Int
) : LengthAwareIteration

data class IterationImpl(override val position: Long) : Iteration

fun createIteration(position: Long, iterationLength: Int): Iteration = LengthAwareIteration(position, iterationLength)

fun createIteration(position: Long): Iteration = IterationImpl(position)
