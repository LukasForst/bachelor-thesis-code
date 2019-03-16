package pw.forst.olb.core.evaluator.leastsquares

/**
 * Credit - https://github.com/odinsbane/least-squares-in-java/blob/master/src/main/java/org/orangepalantir/leastsquares/Function.java
 * */
interface Function {
    val nParameters: Int
    val nInputs: Int
    /**
     * Returns the functions evaluated at the specific parameter set
     * @return needs to evaluate the function
     */
    fun evaluate(values: DoubleArray, parameters: DoubleArray): Double
}

