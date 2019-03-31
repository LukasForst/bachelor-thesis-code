package pw.forst.olb.core.predict.fitting

import pw.forst.olb.core.predict.fitting.parametrization.X
import pw.forst.olb.core.predict.fitting.parametrization.Y


interface PredictionWithParameter<T> : Prediction {

    fun predict(data: Map<X, Y>, toPredict: X, params: T? = null): Y?

    override fun predict(data: Map<X, Y>, toPredict: X): Y? = predict(data, toPredict, null)
}
