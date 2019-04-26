package pw.forst.olb.simulation.input.data

import pw.forst.olb.common.dto.GenericPlan
import pw.forst.olb.common.dto.TimeImpl
import pw.forst.olb.common.dto.impl.JobWithHistoryImpl
import pw.forst.olb.common.dto.impl.LengthAwareIterationImpl
import pw.forst.olb.common.dto.job.Client
import pw.forst.olb.common.dto.job.JobParameters
import pw.forst.olb.common.dto.job.JobWithHistory
import pw.forst.olb.common.extensions.averageByInt
import pw.forst.olb.simulation.extensions.createTimesMap
import pw.forst.olb.simulation.extensions.toLengthAwareIterationData
import java.util.UUID
import java.util.concurrent.TimeUnit

class JobWithHistoryFactory {

    fun create(runtimeInfo: AlgorithmRuntimeInfo, genericPlan: GenericPlan, jobParameters: JobParameters, client: Client): JobWithHistory {
        val iterationData = runtimeInfo.toLengthAwareIterationData()

        return JobWithHistoryImpl(
            plan = genericPlan,
            schedulingStartedTime = TimeImpl(0, TimeUnit.SECONDS),
            averageIteration = LengthAwareIterationImpl(0, iterationData.keys.averageByInt { it.iterationLengthInMls }.toInt()),
            jobValueDuringIterations = iterationData.mapKeys { it.key },
            iterationLengthInTimes = iterationData.keys.createTimesMap(genericPlan.startTime, genericPlan.timeIncrement),
            parameters = jobParameters,
            client = client,
            name = runtimeInfo.name,
            uuid = UUID.randomUUID()
        )
    }
}
