package pw.forst.olb.core.predict.fitting

import mu.KLogging

abstract class AbstractPrediction<T> : PredictionWithParameter<T> {

    private companion object : KLogging()


    override fun predict(data: Map<X, Y>, toPredict: X, params: T?): Y? =
        runCatching { predictUnSafe(data, toPredict, params) }
            .onFailure(logFailure)
            .getOrNull()


    protected open val logFailure: (Throwable) -> Unit = { logger.error(it) { "Error while running polynomial prediction!" } }

    protected abstract fun predictUnSafe(data: Map<X, Y>, toPredict: X, params: T?): Y?

}
