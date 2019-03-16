@file:Suppress("MemberVisibilityCanBePrivate", "PrivatePropertyName", "SpellCheckingInspection", "LocalVariableName", "unused")

package pw.forst.olb.core.predict.leastsquares

import Jama.Matrix
import mu.KLogging

/**
 * Credits - https://github.com/odinsbane/least-squares-in-java/blob/master/src/main/java/org/orangepalantir/leastsquares/fitters/NonLinearSolver.java converted into Kotlin
 * */
class NonLinearSolver(private var FUNCTION: Function) {

    private companion object : KLogging()

    /**
     * Ignores second derivatives, not very good.
     * for solving non-linear least squares fit of f(x:A) = z with sets of x,z data points.
     */
    private lateinit var X: Array<DoubleArray>    //values
    private lateinit var A: DoubleArray
    //Parameter Set
    private lateinit var Z: DoubleArray    //output vaues
    private lateinit var ERROR: DoubleArray
    private lateinit var DERIVATIVES: Array<DoubleArray>

    private var DELTA = 0.000001   //used for calculating derivatives
    private var MIN_ERROR = 1e-6 //for evaluating
    private var MIN_CHANGE = 1e-6    //minimumchanges
    private var STEP = 0.1
    private var MAX_ITERATIONS = 10000.0
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

    fun calculateDerivatives() {
        val abefore = DoubleArray(A.size)
        val aafter = DoubleArray(A.size)
        System.arraycopy(A, 0, abefore, 0, A.size)
        System.arraycopy(A, 0, aafter, 0, A.size)
        for (j in A.indices) {
            aafter[j] += DELTA
            abefore[j] -= DELTA

            if (j > 0) {
                aafter[j - 1] = A[j - 1]
                abefore[j - 1] = A[j - 1]
            }

            for (i in Z.indices) {
                DERIVATIVES[i][j] = (FUNCTION.evaluate(X[i], aafter) - FUNCTION.evaluate(X[i], abefore)) / (2 * DELTA)
            }
        }
    }


    /**
     * Takes the current error, and the current parameter set and calculates the
     * changes, then returns the maximum changed value
     */
    fun iterateValues(): Double {
        val matrix = Array(A.size) { DoubleArray(A.size) }
        val right = DoubleArray(A.size)
        for (i in A.indices) {
            for (k in Z.indices) {
                right[i] += ERROR[k] * DERIVATIVES[k][i]
                for (l in A.indices) {
                    matrix[i][l] += DERIVATIVES[k][i] * DERIVATIVES[k][l]
                }
            }

        }


        val coeff = Matrix(matrix)
        val sols = Matrix(right, A.size)
        val adecom = coeff.lu()

        val out = adecom.solve(sols)

        val values = out.array

        var max_change = Math.abs(values[0][0])
        for (i in A.indices) {
            max_change = if (max_change > Math.abs(values[i][0])) max_change else Math.abs(values[i][0])
            A[i] += values[i][0] * STEP
        }

        return max_change


    }

    fun iterateValuesB() {
        val system = Matrix(DERIVATIVES)
        val params = Matrix(ERROR, ERROR.size)
        //params = params.times(-1.);
        val out = system.solve(params)

        val values = out.array

        for (i in A.indices) {
            A[i] += values[i][0] * 0.001
        }

    }


    fun initializeWorkspace() {
        ERROR = DoubleArray(Z.size)
        DERIVATIVES = Array(Z.size) { DoubleArray(A.size) }
    }

    /**
     * Main routine, call this and the Parameters are iterated until it is finished.
     */
    fun fitData() {
        initializeWorkspace()
        var i = 0
        var changes: Double
        var last_error = java.lang.Double.MAX_VALUE
        while (i < MAX_ITERATIONS) {

            val e = calculateErrors()
            if (e < MIN_ERROR) {
                break
            }
            if (e > last_error) {
                logger.error { "Error increased: consider smaller step size." }
                break
            }
            last_error = e
            calculateDerivatives()
            try {
                changes = iterateValues()
                if (changes < MIN_CHANGE)
                    break
            } catch (exc: Exception) {
                throw RuntimeException(exc)
            }

            i++
        }
        if (i.toDouble() == MAX_ITERATIONS) {
            logger.error { "Warning: Maximum iteration reached." }
        }


    }

    fun setStepSize(step: Double) {
        STEP = step
    }

    fun setMinError(error: Double) {
        MIN_ERROR = error
    }

    /**
     * When the parameters change less than the change parameter the program will stop iterating
     *
     * @param change
     */
    fun setMinChange(change: Double) {
        MIN_CHANGE = change
    }

}
