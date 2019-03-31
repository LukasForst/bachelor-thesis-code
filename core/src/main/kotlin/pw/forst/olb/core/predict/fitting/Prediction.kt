package pw.forst.olb.core.predict.fitting

interface Prediction {

    fun predict(data: Map<X, Y>, toPredict: X): Y?

}
