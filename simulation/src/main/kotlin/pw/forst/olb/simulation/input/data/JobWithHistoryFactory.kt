package pw.forst.olb.simulation.input.data

import pw.forst.olb.common.dto.GenericPlan
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.TimeImpl
import pw.forst.olb.common.dto.impl.IterationImpl
import pw.forst.olb.common.dto.impl.JobValueImpl
import pw.forst.olb.common.dto.impl.JobWithHistoryImpl
import pw.forst.olb.common.dto.impl.LengthAwareIterationImpl
import pw.forst.olb.common.dto.job.Client
import pw.forst.olb.common.dto.job.Iteration
import pw.forst.olb.common.dto.job.JobParameters
import pw.forst.olb.common.dto.job.JobValue
import pw.forst.olb.common.dto.job.JobWithHistory
import pw.forst.olb.common.dto.job.LengthAwareIteration
import pw.forst.olb.common.dto.resources.ResourcesAllocation
import pw.forst.olb.common.extensions.averageByInt
import pw.forst.olb.common.extensions.mapKeysAndValues
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class JobWithHistoryFactory {

    fun create(runtimeInfo: AlgorithmRuntimeInfo, genericPlan: GenericPlan, jobParameters: JobParameters, client: Client): JobWithHistory {
        val iterationData = runtimeInfo.data.associate {
            LengthAwareIterationImpl(it.index, it.iterationLength) to JobValueImpl(it.cost)
        }

        return JobWithHistoryImpl(
            plan = genericPlan,
            schedulingStartedTime = TimeImpl(0, TimeUnit.SECONDS),
            averageIteration = LengthAwareIterationImpl(0, iterationData.keys.averageByInt { it.iterationLengthInMls }.toInt()),
            jobValueDuringIterations = iterationData.mapKeysAndValues({ it.key as Iteration }, { it.value as JobValue }),
            iterationLengthInTimes = iterationData.keys.createTimesMap(genericPlan.startTime, genericPlan.timeIncrement),
            parameters = jobParameters,
            client = client,
            name = runtimeInfo.name,
            uuid = UUID.randomUUID(),
            _iterationAllocationQuocient = ::iterationAllocationQuocient
        )
    }

    private fun Collection<LengthAwareIteration>.createTimesMap(startTime: Time, timeIncrement: Time): Map<Time, Collection<LengthAwareIteration>> {
        data class DataFold(val currTimeInMs: Int, val currTime: Time, val map: MutableMap<Time, MutableCollection<LengthAwareIteration>>)

        val msInS = timeIncrement.units.toMillis(timeIncrement.position).toInt()

        val (_, _, map) =
            this.fold(DataFold(0, startTime, mutableMapOf(startTime to mutableListOf()))) { (currTimeInMs, currTime, map), x ->
                val diff = currTimeInMs + x.iterationLengthInMls
                if (diff <= msInS) {
                    map[currTime]!!.add(x)
                    DataFold(currTimeInMs + x.iterationLengthInMls, currTime, map)
                } else {
                    val newTime = currTime + timeIncrement
                    map[newTime] = mutableListOf(x)
                    DataFold(currTimeInMs + x.iterationLengthInMls - msInS, newTime, map)
                }
            }

        return map.toMap()
    }

    private fun iterationAllocationQuocient(iteration: Iteration, allocation: ResourcesAllocation, jobWithHistory: JobWithHistory): Iteration {
        val averageIncrement = jobWithHistory.averageIteration.iterationLengthInMls * jobWithHistory.plan.timeIncrement.units.toMillis(jobWithHistory.plan.timeIncrement.position)
        val allocationAwareIncrement = (averageIncrement * allocation.cpuResources.cpuValue).roundToInt()
        return iteration + IterationImpl(allocationAwareIncrement)
    }
}
