package pw.forst.olb.core.extensions

import pw.forst.olb.common.dto.AllocationPlan
import pw.forst.olb.common.extensions.maxValueBy

fun AllocationPlan.prettyFormat(): String {
    val poolData = this.resourcesPools.associate { pool ->
        pool to this.timeSchedule.mapValues { (_, asgs) ->
            asgs.filter { it.allocation.provider == pool.provider }.sortedBy { it.job.name }.flatMap { allocation ->
                (0 until allocation.allocation.cpuResources.cpuValue.toInt()).map { allocation.job.name.format(2) }
            }.let {
                it.sorted() + (0 until (pool.cpuResources.cpuValue - it.size).toInt()).map { "".format(2) }
            }
        }
    }.mapValues { (_, times) ->
        val first = times.values.random().size

        times.keys.sorted().fold((0 until first).map { "" }) { final, x ->
            val currentProcessed = times.getValue(x)
            assert(final.size == currentProcessed.size)

            final.zip(currentProcessed) { f, p -> "$f|${p.format(2)}" }
        }
    }

    val largestKey = poolData.keys.maxValueBy { it.provider.name.length }!!
    return poolData.keys.sortedBy { it.provider.name }.joinToString(System.lineSeparator()) { pool ->
        poolData.getValue(pool).fold("") { acc, s -> "$acc${pool.provider.name.format(largestKey)}|$s\n" }
    }
}

private fun Double.format(digits: Int) = java.lang.String.format("%${digits}d", this)
private fun Int.format(digits: Int) = java.lang.String.format("%${digits}d", this)
private fun String.format(digits: Int) = java.lang.String.format("%${digits}s", this)
