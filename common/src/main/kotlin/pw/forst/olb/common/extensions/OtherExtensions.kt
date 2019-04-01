package pw.forst.olb.common.extensions

import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.impl.completeJobAssignment
import pw.forst.olb.common.dto.impl.createEmptyResourcesAllocation
import pw.forst.olb.common.dto.job.CompleteJobAssignment
import pw.forst.olb.common.dto.job.toCpuResources
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Optional

/**
 * Returns value or null from Optional. Useful when using kotlin-like T? and Optional<T>
 * */
fun <T> Optional<T>.orNull(): T? = this.orElse(null)

/**
 * Calls the specified function [block] only when `this` value is null and then returns `this` value.
 */
inline fun <T : Any?> T.whenNull(block: () -> Unit): T {
    if (this == null) block()
    return this
}

/**
 * Executes [block] iff this (result of previous method) is true. Returns given Boolean.
 * */
inline fun Boolean.whenTrue(block: () -> Unit): Boolean {
    if (this) block()
    return this
}

/**
 * Executes [block] iff this (result of previous method) is false. Returns given Boolean.
 * */
inline fun Boolean.whenFalse(block: () -> Unit): Boolean {
    if (!this) block()
    return this
}

/**
 * Creates a single element list from [this].
 */
fun <T : Any> T.asList() = listOf(this)


/**
 * Returns true when there is at least one element x for which *x in this* and *x in other* returns true.
 */
fun <T : Comparable<T>> ClosedRange<T>.intersects(other: ClosedRange<T>): Boolean {
    return this.endInclusive >= other.start && this.start <= other.endInclusive
}

/**
 * Convert java.util.Date to LocalDate
 */
fun Date.toLocalDate(): LocalDate = LocalDate.from(Instant.ofEpochMilli(this.time).atZone(ZoneId.systemDefault()))


fun Pair<Time, Time>.duration(): Time = this.second - this.first

fun Collection<CompleteJobAssignment>.flattenAssignments(): Collection<CompleteJobAssignment> =
    this.groupBy { it.job to it.time }
        .map { (job, asg) ->
            val first = asg.first()
            val init = createEmptyResourcesAllocation(first.allocation.provider, first.job.parameters.jobType.toCpuResources())
            val finalAllocation = asg.fold(init) { acc, jobAssignment -> acc + jobAssignment.allocation }
            completeJobAssignment(job = job.first, time = job.second, allocation = finalAllocation)
        }


