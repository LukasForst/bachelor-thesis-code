//package pw.forst.olb.core.predict.job
//
//import pw.forst.olb.core.predict.fitting.predictors.LinearRegression
//import pw.forst.olb.core.predict.fitting.NonLinearHyperbolaRegression
//import pw.forst.olb.core.predict.fitting.predictors.PolynomialRegression
//
//object JobValuePredictorFactory {
//
//    fun linearRegression(): JobValuePrediction = JobValuePredictor(LinearRegression(), null)
//
//    fun polynomialRegression(polynomDegree: Int? = null): JobValuePrediction = JobValuePredictor(PolynomialRegression(), polynomDegree)
//
//    fun nonLinearHyperbolaRegression(initialParameters: DoubleArray? = null): JobValuePrediction = JobValuePredictor(NonLinearHyperbolaRegression(), initialParameters)
//}
