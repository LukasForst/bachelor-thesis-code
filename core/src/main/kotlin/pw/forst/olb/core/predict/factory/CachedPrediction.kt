package pw.forst.olb.core.predict.factory

import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.Iteration
import pw.forst.olb.common.dto.job.JobValue
import pw.forst.olb.common.dto.job.JobWithHistory
import pw.forst.olb.common.dto.resources.ResourcesAllocation

interface CachedPrediction {

    val job: JobWithHistory

    fun get(iteration: Iteration): JobValue

    fun getLastIteration(time: Time): Iteration

    fun getNextIteration(from: Iteration, allocation: ResourcesAllocation): Iteration
}
