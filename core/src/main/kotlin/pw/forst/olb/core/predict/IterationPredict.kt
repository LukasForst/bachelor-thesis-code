package pw.forst.olb.core.predict

import mu.KLogging
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.impl.createIteration
import pw.forst.olb.common.dto.job.Iteration
import pw.forst.olb.common.dto.job.JobMetaData
import pw.forst.olb.common.dto.resources.ResourcesAllocation
import pw.forst.olb.common.extensions.averageByInt
import pw.forst.olb.common.extensions.mapKeysAndValues
import pw.forst.olb.core.predict.fitting.Prediction

class IterationPredict(
    private val prediction: Prediction
) {

    private companion object : KLogging()

    @Suppress("unused") // will be used in the future
    fun getIteration(metadata: JobMetaData, time: Time): Iteration {
        val maxTime = metadata.iterationTime.keys.max()

        return if (maxTime == null) {
            //cannot be determined
            createIteration(position = 0, iterationLength = Int.MAX_VALUE).also { logger.warn { "Iteration length could not be determined! Provide data first!" } }
        } else {
            val iterationAvg = metadata.iterationTime.mapValues { (_, v) -> v.averageByInt { it.iterationLengthInMls } }
            val timeBetweenLastAndThis = time - maxTime
            val predictedLength = tryPredictLength(metadata, time, iterationAvg) ?: iterationAvg.values.average()

            createIteration(position = iterationPosition(predictedLength, timeBetweenLastAndThis), iterationLength = predictedLength.toInt())
        }
    }

    fun getIteration(metadata: JobMetaData, time: Time, resourcesAllocation: ResourcesAllocation): Iteration {
        val maxTime = metadata.iterationTime.keys.max()

        return if (maxTime == null) {
            //cannot be determined
            createIteration(position = 0, iterationLength = Int.MAX_VALUE).also { logger.warn { "Iteration length could not be determined! Provide data first!" } }
        } else {
            val iterationAvg = metadata.iterationTime.mapValues { (_, v) -> v.averageByInt { it.iterationLengthInMls } }
            val timeBetweenLastAndThis = time - maxTime
            val predictedLength = (tryPredictLength(metadata, time, iterationAvg) ?: iterationAvg.values.average()) * getMagicConstant(metadata, time, resourcesAllocation)

            createIteration(position = iterationPosition(predictedLength, timeBetweenLastAndThis), iterationLength = predictedLength.toInt())
        }
    }

    @Suppress("UNUSED_PARAMETER") // will be implemented in the future
    private fun getMagicConstant(metadata: JobMetaData, time: Time, resourcesAllocation: ResourcesAllocation): Double = 1 / resourcesAllocation.cpuResources.cpuValue

    private fun iterationPosition(length: Double, last: Time): Long = (last.position * length * 1000).toLong()

    private fun tryPredictLength(metadata: JobMetaData, time: Time, iterationAvg: Map<Time, Double>): Double? = metadata.iterationTime
        .mapKeysAndValues({ it.key.position.toDouble() }, { iterationAvg.getValue(it.key) })
        .let { prediction.predict(it, time.position.toDouble()) }


}
