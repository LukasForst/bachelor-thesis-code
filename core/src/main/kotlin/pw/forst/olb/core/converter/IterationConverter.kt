package pw.forst.olb.core.converter

import mu.KLogging
import pw.forst.olb.common.dto.Time
import pw.forst.olb.common.dto.job.Iteration
import pw.forst.olb.common.dto.job.JobMetaData
import pw.forst.olb.common.dto.job.impl.createIteration
import pw.forst.olb.common.extensions.averageByInt
import pw.forst.olb.common.extensions.mapKeysAndValues
import pw.forst.olb.core.predict.fitting.Prediction

class IterationConverter(
    private val prediction: Prediction
) {

    private companion object : KLogging()

    fun getIterationPositionForTime(metadata: JobMetaData, time: Time): Iteration {
        val maxTime = metadata.iterationTime.keys.max()

        return if (maxTime == null) {
            //cannot be determined
            createIteration(position = 0, iterationLength = Int.MAX_VALUE).also { logger.warn { "Iteration length could not be determined! Provider data first!" } }
        } else {
            val timeBetweenLastAndThis = time - maxTime
            val predictedLength = tryPredict(metadata, time) ?: fallBackPredict(metadata)

            createIteration(position = (timeBetweenLastAndThis.seconds * predictedLength * 100000).toLong(), iterationLength = predictedLength.toInt())
        }
    }


    private fun fallBackPredict(metadata: JobMetaData): Double = metadata.iterationTime.values.flatten().averageByInt { it.iterationLengthInMls }

    private fun tryPredict(metadata: JobMetaData, time: Time): Double? = metadata.iterationTime
        .mapKeysAndValues({ it.seconds.toDouble() }, { value -> value.averageByInt { it.iterationLengthInMls } })
        .let { prediction.predict(it, time.seconds.toDouble()) }

}
