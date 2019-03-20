package pw.forst.olb.simulation.prediction

import pw.forst.olb.core.predict.fitting.LinearRegression
import pw.forst.olb.core.predict.fitting.NonLinearHyperbolaRegression
import pw.forst.olb.core.predict.fitting.PolynomialRegression

fun main() {
    val inputFolder = "/home/lukas/repos/bp/job-data/test-input"
    val outputFolder = "/home/lukas/repos/bp/job-data/fitted-out"

    val predictionAlgorithms = listOf(
        NonLinearHyperbolaRegression(),
        PolynomialRegression(),
        LinearRegression()
    )
    val simulations = PredictionSimulation()

    val results = InputProvider().readFolder(inputFolder)
        .mapValues { (_, data) -> simulations.run(data, predictionAlgorithms) }

    OutputWritter().write(outputFolder, results)
}
