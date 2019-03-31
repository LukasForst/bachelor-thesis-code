package pw.forst.olb.core.predict.fitting

import mu.KLogging
import org.apache.commons.math3.stat.regression.SimpleRegression

class LinearRegression : AbstractPrediction<Nothing>() {

    private companion object : KLogging()

    override val logFailure: (Throwable) -> Unit = { logger.error(it) { "Error while running linear prediction!" } }

    override fun predictUnSafe(data: Map<X, Y>, toPredict: X, params: Nothing?): Y? {
        if (data.isEmpty()) return null.also { logger.warn { "No data to predict!" } }

        val regression = SimpleRegression()
        data.forEach { (x, y) -> regression.addData(x, y) }
        val predicted = regression.predict(toPredict)
        if (predicted == Double.NaN) return null.also { logger.info { "It was not possible to create prediction!" } }

        return predicted
    }

}
