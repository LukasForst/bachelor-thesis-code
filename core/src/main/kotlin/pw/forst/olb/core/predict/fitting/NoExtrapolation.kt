package pw.forst.olb.core.predict.fitting

class NoExtrapolation : AbstractPrediction<Int>() {
    override fun predictUnSafe(data: Map<X, Y>, toPredict: X, params: Int?): Y? = params?.toDouble() ?: 0.0
}
