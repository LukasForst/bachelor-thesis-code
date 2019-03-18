package pw.forst.olb.core.predict.job

import pw.forst.olb.common.dto.job.Iteration
import pw.forst.olb.common.dto.job.JobValue
import pw.forst.olb.common.dto.job.impl.JobValueImpl
import pw.forst.olb.common.dto.scheduling.JobPlanView
import pw.forst.olb.common.extensions.mapKeysAndValues
import pw.forst.olb.core.predict.fitting.LinearRegression
import pw.forst.olb.core.predict.fitting.NonLinearHyperbolaRegression
import pw.forst.olb.core.predict.fitting.PolynomialRegression
import pw.forst.olb.core.predict.fitting.PredictionWithParameter

class JobValuePredictionFactory<T> private constructor(
    private val prediction: PredictionWithParameter<T>,
    private val predictionParameter: T?
) : JobValuePrediction {

    companion object {
        fun linearRegression(): JobValuePrediction = JobValuePredictionFactory(LinearRegression(), null)

        fun polynomialRegression(polynomDegree: Int? = null): JobValuePrediction = JobValuePredictionFactory(PolynomialRegression(), polynomDegree)

        fun nonLinearHyperbolaRegression(initialParameters: DoubleArray? = null): JobValuePrediction = JobValuePredictionFactory(NonLinearHyperbolaRegression(), initialParameters)
    }

    override fun predict(view: JobPlanView, iteration: Iteration): JobValue? {
        val predictionSet = view.values.mapKeysAndValues({ it.position.toDouble() }, { it.value })
        return prediction.predict(predictionSet, iteration.position.toDouble(), predictionParameter)?.let { JobValueImpl(it) }
    }
}
