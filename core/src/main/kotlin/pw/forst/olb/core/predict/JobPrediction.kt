package pw.forst.olb.core.predict

import mu.KLogging
import pw.forst.olb.common.dto.impl.JobValueImpl
import pw.forst.olb.common.dto.impl.createIteration
import pw.forst.olb.common.dto.job.Iteration
import pw.forst.olb.common.dto.job.JobWithHistory
import pw.forst.olb.common.extensions.mapKeysAndValues
import pw.forst.olb.common.extensions.maxValueBy
import pw.forst.olb.core.predict.factory.CachedPrediction
import pw.forst.olb.core.predict.factory.CachedPredictionImpl
import pw.forst.olb.core.predict.fitting.HyperbolaPredictionBuilder
import pw.forst.olb.core.predict.fitting.computeY

class JobPrediction {

    private companion object : KLogging()

    fun createPrediction(job: JobWithHistory, lastPredictedIteration: Iteration): CachedPrediction? {
        if (job.jobValueDuringIterations.isEmpty()) return null

        val valuePrediction = job.jobValueDuringIterations
            .mapKeysAndValues({ it.key.position.toDouble() }, { it.value.value })

        val prediction = HyperbolaPredictionBuilder.create()
            .build()
        val parameters = prediction.obtainParameters(valuePrediction) ?: return null.also { logger.warn { "It was not possible to obtain prediction for job ${job.name}" } }

        val predicted = (job.jobValueDuringIterations.keys.maxValueBy { it.position }!! until lastPredictedIteration.position).associate {
            createIteration(it) to JobValueImpl(parameters.computeY(it))
        }

        return CachedPredictionImpl(
            job = job,
            values = predicted,
            backupLazyEvaluation = null
        )
    }
}
