package pw.forst.olb.core.predict.factory

import mu.KLogging
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.Iteration
import pw.forst.olb.common.dto.job.JobValue
import pw.forst.olb.common.dto.job.JobWithHistory
import pw.forst.olb.common.dto.resources.ResourcesAllocation
import pw.forst.olb.common.extensions.whenNull

class CachedPredictionImpl(
    override val job: JobWithHistory,
    private val values: Map<Iteration, JobValue>,
    private val backupLazyEvaluation: ((Iteration) -> JobValue?)?
) : CachedPrediction {

    private companion object : KLogging()

    override fun get(iteration: Iteration): JobValue = values[iteration].whenNull { logger.warn { "Using lazy evaluation for job: $job, this should be avoided!" } }
        ?: backupLazyEvaluation?.invoke(iteration)
        ?: throw IllegalStateException("It is not possible to obtain prediction for job: ${job.name} - iteration ${iteration.position}")

    override fun getLastIteration(time: Time): Iteration {
        return job.iterationLengthInTimes[time]?.maxBy { it.position }
            ?: job.iterationsInTimes[time]?.maxBy { it.position }
            ?: throw IllegalStateException("It is not possible to obtain last iteration prediction for job: ${job.name} - time $time")
    }

    override fun getNextIteration(from: Iteration, allocation: ResourcesAllocation): Iteration = job.iterationAllocationQuocient(from, allocation)
}
