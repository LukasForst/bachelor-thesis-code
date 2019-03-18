package pw.forst.olb.common.dto.job.impl

import pw.forst.olb.common.dto.job.Iteration

data class IterationImpl(override val position: Long, override val iterationLengthInMls: Int) : Iteration

fun createIteration(position: Long, iterationLength: Int): Iteration = IterationImpl(position, iterationLength)
