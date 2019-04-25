package pw.forst.olb.common.extensions

import pw.forst.olb.common.dto.AllocationPlan
import pw.forst.olb.common.dto.CostImpl
import pw.forst.olb.common.dto.sum
import kotlin.math.max

fun AllocationPlan.prettyFormat(printJobsData: Boolean = true): String {
    if (this.timeSchedule.isEmpty()) throw IllegalStateException("No plan produced! Check your constraints!")

    val maxFormatInt = max(this.timeSchedule.keys.max()!!.position.toString().length, this.jobs.maxValueBy { it.name.length }!!)

    val poolData = this.resourcesPools.associate { pool ->
        pool to this.timeSchedule.mapValues { (_, asgs) ->
            asgs.filter { it.allocation.provider == pool.provider }.sortedBy { it.job.name }.flatMap { allocation ->
                (0 until allocation.allocation.cpuResources.cpuValue.toInt()).map { allocation.job.name.format(maxFormatInt) }
            }.let {
                it.sorted() + (0 until (pool.cpuResources.cpuValue.toInt() - it.size)).map { "".format(maxFormatInt) }
            }
        }
    }.mapValues { (_, times) ->
        val first = times.values.random().size

        times.keys.sorted().fold((0 until first).map { "" }) { final, x ->
            val currentProcessed = times.getValue(x)
            assert(final.size == currentProcessed.size) { "Final: ${final.size}, Current: ${currentProcessed.size}" }

            final.zip(currentProcessed) { f, p -> "$f|${p.format(maxFormatInt)}" }
        }
    }

    val largestKey = poolData.keys.maxValueBy { it.provider.name.length }!!
    val plan = poolData.keys.sortedBy { it.provider.name }.joinToString(l) { pool ->
        poolData.getValue(pool).fold("") { acc, s -> "$acc${pool.provider.name.format(largestKey)}|$s|$l" }
    }

    val times = "Times:".format(largestKey) + "||" + this.timeSchedule.keys.sorted().joinToString("|") { it.position.toInt().format(maxFormatInt) } + "|"

    val separator = "-".repeat(largestKey) + "||" + ("-".repeat(maxFormatInt) + "|").repeat(this.timeSchedule.size)

    val planCost = "Total plan cost: ${this.cost}$"

    val jobData = if (printJobsData) this.jobs.joinToString(l) { job ->
        val timeCosts = this.timeSchedule.mapValues { (_, asgs) ->
            val relevant = asgs.filter { it.job == job }.map { it.allocation }
            relevant.map { it.cost }.sum()
        }
        val (min, max) = timeCosts.filterValues { it != CostImpl(0.0) }.keys.minMaxBy { it.position }!!
        val totalCost = timeCosts.values.sum()

        "${job.name} -- $job$l" +
                "First assignment: ${min.position}$l" +
                "Last Assignment: ${max.position}$l" +
                "Duration: ${(max - min).position}$l" +
                "Max time allowed: ${job.parameters.maxTime.position}$l" +
                "Cost: ${totalCost.value}$l" +
                "Max Cost: ${job.parameters.maxCost.value}$l"
    } else ""


    return "$times$l$separator$l$plan$l$planCost$l$jobData"
}

private val l = System.lineSeparator()

private fun Double.format(digits: Int) = genericFormat(digits, "d", this)
private fun Int.format(digits: Int) = genericFormat(digits, "d", this)
private fun String.format(digits: Int) = genericFormat(digits, "s", this)

private fun genericFormat(digits: Int, format: String, data: Any) = String.format("%$digits$format", data)
