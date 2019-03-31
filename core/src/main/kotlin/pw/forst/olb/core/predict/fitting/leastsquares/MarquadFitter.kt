package pw.forst.olb.core.predict.fitting.leastsquares

import Jama.Matrix


class MarquardtFitter(internal var FUNCTION: Function) {
    /**
     * for solving non-linear least squares fit of f(x:A) = z with sets of x,z data points.
     */
    internal lateinit var X: Array<DoubleArray>    //values
    internal lateinit var A: DoubleArray
    //Parameter Set
    internal lateinit var Z: DoubleArray    //output vaues
    internal lateinit var ERROR: DoubleArray

    internal lateinit var DERIVATIVES: Array<DoubleArray>
    internal lateinit var ALPHA_PRIME: Array<DoubleArray>
    internal lateinit var LAMBDA: DoubleArray
    internal lateinit var BETA: DoubleArray

    internal var DELTA = 0.000001   //used for calculating derivatives
    internal var MINERROR = 1e-9 //for evaluating
    internal var MINCHANGE = 1e-3    //minimumchanges

    var ITERATIONS = 0
    /**
     * Gets the current set of parameters values.
     */
    /**
     *
     */
    var parameters: DoubleArray
        get() = A
        set(parameters) {
            if (parameters.size != FUNCTION.nParameters)
                throw IllegalArgumentException("the number of parameters must equal the required number for the function: " + FUNCTION.nParameters)
            A = DoubleArray(parameters.size)
            System.arraycopy(parameters, 0, A, 0, parameters.size)
        }

    val uncertainty: DoubleArray
        get() = DoubleArray(0)

    /**
     * Sets the values of of the original data points that are going to be fit.
     */
    fun setData(xvalues: Array<DoubleArray>, zvalues: DoubleArray) {

        if (xvalues.size != zvalues.size)
            throw IllegalArgumentException("there must be 1 z value for each set of x values")
        else if (xvalues[0].size != FUNCTION.nInputs)
            throw IllegalArgumentException("The length of parameters is longer that the parameters accepted by the function")
        X = xvalues
        Z = zvalues

    }

    /**
     * returns the cumulative error for current values
     */
    fun calculateErrors(): Double {
        var new_error = 0.0
        for (i in Z.indices) {

            val v = FUNCTION.evaluate(X[i], A)
            ERROR[i] = Z[i] - v
            new_error += Math.pow(ERROR[i], 2.0)
        }
        return new_error

    }

    /**
     * Given a set of parameters, and inputs, calculates the derivative
     * of the k'th parameter
     * d/d a_k (F)
     *
     * @param k - index of the parameter that the derivative is being taken of
     * @param params - array that will be used to calculate derivatives, note params will be modified.
     * @param x - set of values to use.
     * @return derivative of function
     */
    fun calculateDerivative(k: Int, params: DoubleArray, x: DoubleArray): Double {
        params[k] -= DELTA
        val b = FUNCTION.evaluate(x, params)
        params[k] += 2 * DELTA
        val a = FUNCTION.evaluate(x, params)
        params[k] -= DELTA
        return (a - b) / (2 * DELTA)

    }

    /**
     * Creates an array of derivatives since each one is used 3x's
     */
    fun calculateDerivatives() {
        val working = DoubleArray(A.size)
        System.arraycopy(A, 0, working, 0, A.size)
        for (j in A.indices) {

            for (i in Z.indices) {
                DERIVATIVES[i][j] = calculateDerivative(j, working, X[i])
            }
        }


    }

    /**
     *
     * NOT USED.
     * Given a set of parameters, and inputs, calculates the
     * second derivatives
     * d^2/d a_l d a_k (F)
     *
     * @param k - index of the parameter that the derivative is being taken of
     * @param params - array that will be used to calculate derivatives, note params will be modified.
     * @param x - set of values to use.
     */
    fun calculateSecondDerivative(l: Int, k: Int, params: DoubleArray, x: DoubleArray): Double {
        params[l] -= DELTA
        val b = calculateDerivative(k, params, x)

        params[l] += 2 * DELTA
        val a = calculateDerivative(k, params, x)


        params[l] -= DELTA
        return (a - b) / (2 * DELTA)

    }

    fun createBetaMatrix() {
        BETA = DoubleArray(A.size)
        val working = DoubleArray(A.size)
        System.arraycopy(A, 0, working, 0, A.size)

        for (k in BETA.indices) {
            for (i in X.indices) {

                BETA[k] += ERROR[i] * DERIVATIVES[i][k]

            }
        }

    }

    fun createAlphaPrimeMatrix() {
        ALPHA_PRIME = Array(A.size) { DoubleArray(A.size) }

        val n = A.size
        for (k in 0 until n) {
            for (l in 0 until n) {

                for (i in X.indices)
                    ALPHA_PRIME[l][k] += DERIVATIVES[i][k] * DERIVATIVES[i][l]

                if (k == l)
                    ALPHA_PRIME[l][k] = ALPHA_PRIME[l][k] * (1 + LAMBDA[k])

            }

        }


    }

    /**
     * Takes the current error, and the current parameter set and calculates the
     * changes, then returns the maximum changed value
     */
    fun iterateValues() {

        calculateDerivatives()

        createBetaMatrix()
        createAlphaPrimeMatrix()

        val alpha_matrix = Matrix(ALPHA_PRIME)
        val beta = Matrix(BETA, BETA.size)

        val out = alpha_matrix.solve(beta)

        val delta_a = out.array

        for (i in A.indices)
            A[i] += delta_a[i][0]


    }


    fun initializeWorkspace() {
        ERROR = DoubleArray(Z.size)
        DERIVATIVES = Array(Z.size) { DoubleArray(A.size) }
        LAMBDA = DoubleArray(A.size)
        for (i in A.indices)
            LAMBDA[i] = 0.01
    }

    /**
     * Main routine, call this and the Parameters are iterated until it is finished.
     */
    fun fitData() {
        initializeWorkspace()
        var error = calculateErrors()
        var nerror: Double
        var value: Double
        val acopy = DoubleArray(A.size)

        var i: Int
        i = 0
        while (i < 10000) {

            try {
                System.arraycopy(A, 0, acopy, 0, A.size)
                iterateValues()

            } catch (exc: Exception) {
                println("Broke after $i iterations")
                printMatrix()
                //printMatrix();
                throw RuntimeException(exc)
            }

            nerror = calculateErrors()

            value = error - nerror


            if (value < 0) {

                //reject changes
                System.arraycopy(acopy, 0, A, 0, acopy.size)
                updateLambda(value)
                nerror = calculateErrors()
            } else {


                if (value < 0.0001) {
                    ITERATIONS = i
                    break
                }
            }

            error = nerror
            i++
        }


    }

    fun updateLambda(value: Double) {

        if (value < 0) {
            for (i in LAMBDA.indices)
                LAMBDA[i] = LAMBDA[i] * 10


        } else {

            for (i in LAMBDA.indices)
                LAMBDA[i] = LAMBDA[i] * 0.1

        }


    }

    /**
     * for stack traces
     */
    fun printMatrix(): String {
        var message = ""
        for (i in ALPHA_PRIME.indices) {
            for (j in 0 until ALPHA_PRIME[0].size) {
                message += ALPHA_PRIME[i][j].toString() + "\t"

            }

            message += "| " + BETA[i] + "\n"
        }
        return message
    }

}
