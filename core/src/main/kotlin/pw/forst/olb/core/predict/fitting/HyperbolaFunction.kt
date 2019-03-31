package pw.forst.olb.core.predict.fitting

class HyperbolaFunction {

    fun modelFunction(parameters: HyperbolicParameters, x: Double, y: Double): Double {
        val (a, b, c) = parameters
        return a * c + a * x + b - y * c
    }

    fun modelFunction(parameters: HyperbolicParameters, x: Int, y: Double): Double = modelFunction(parameters, x.toDouble(), y)

    fun hyperbolicDerivatives(parameters: HyperbolicParameters, x: Double, y: Double): HyperbolicDerivatives {
        val (a, b, c) = parameters
        return HyperbolicDerivatives(
            da = c + x,
            db = 1.0,
            dc = a - y
        )
    }

    fun hyperbolicDerivatives(parameters: HyperbolicParameters, x: Int, y: Double): HyperbolicDerivatives = hyperbolicDerivatives(parameters, x, y)

    fun getY(parameters: HyperbolicParameters, x: Double): Double = parameters.a + parameters.b / (x + parameters.c)

    fun getY(parameters: HyperbolicParameters, x: Int): Double = getY(parameters, x.toDouble())
}

data class HyperbolicDerivatives(
    val da: Double,
    val db: Double,
    val dc: Double
)

data class HyperbolicParameters(
    val a: Double,
    val b: Double,
    val c: Double
) {
    constructor(parameters: DoubleArray) : this(parameters[0], parameters[1], parameters[2])
    constructor(parameters: List<Double>) : this(parameters[0], parameters[1], parameters[2])
}

fun DoubleArray.toHyperbolicParameters(): HyperbolicParameters = HyperbolicParameters(this)
fun List<Double>.toHyperbolicParameters(): HyperbolicParameters = HyperbolicParameters(this)

fun HyperbolicParameters.computeY(x: Double): Double = HyperbolaFunction().getY(this, x)
fun HyperbolicParameters.computeY(x: Int): Double = HyperbolaFunction().getY(this, x)
fun HyperbolicParameters.toDoubleArray(): DoubleArray = doubleArrayOf(this.a, this.b, this.c)


