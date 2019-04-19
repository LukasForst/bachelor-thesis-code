package pw.forst.olb.simulation.extensions

import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.impl.JobValueImpl
import pw.forst.olb.common.dto.impl.LengthAwareIterationImpl
import pw.forst.olb.common.dto.job.JobValue
import pw.forst.olb.common.dto.job.LengthAwareIteration
import pw.forst.olb.simulation.input.data.AlgorithmRuntimeInfo

fun Collection<LengthAwareIteration>.createTimesMap(startTime: Time, timeIncrement: Time): Map<Time, Collection<LengthAwareIteration>> {
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

fun AlgorithmRuntimeInfo.toLengthAwareIterationData(): Map<LengthAwareIteration, JobValue> = this.data.associate {
    LengthAwareIterationImpl(it.index, it.iterationLength) to JobValueImpl(it.cost)
}
