package pw.forst.olb.core.predict.fitting

import pw.forst.olb.core.predict.fitting.parametrization.X
import pw.forst.olb.core.predict.fitting.parametrization.Y

interface Prediction {

    fun predict(data: Map<X, Y>, toPredict: X): Y?

}
