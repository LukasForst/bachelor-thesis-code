package pw.forst.olb.simulation.visualisation

import pw.forst.olb.common.dto.AllocationPlan

class PrettyPrint {

    fun format(plan: AllocationPlan): String {
        val poolData = plan.resourcesPools.associate { pool ->
            pool to plan.timeSchedule.mapValues { (_, asgs) ->
                asgs.filter { it.allocation.provider == pool.provider }.sortedBy { it.job.name }.flatMap { allocation ->
                    (0 until allocation.allocation.cpuResources.cpuValue.toInt()).map { allocation.job.name }
                }.let {
                    (0 until (pool.cpuResources.cpuValue - it.size).toInt()).map { "x" } + it
                }
            }
        }.mapValues { (_, times) ->
            val first = times.values.random().size

            times.keys.sorted().fold((0 until first).map { "" }) { final, x ->
                val currentProcessed = times.getValue(x)
                assert(final.size == currentProcessed.size)

                final.zip(currentProcessed) { f, p -> "$f | $p" }
            }
        }

        return poolData.keys.sortedBy { it.provider.name }.joinToString(System.lineSeparator()) { pool ->
            poolData.getValue(pool).fold("") { acc, s -> "$acc | $s" }
        }
    }
}
