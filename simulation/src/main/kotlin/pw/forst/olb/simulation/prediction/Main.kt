package pw.forst.olb.simulation.prediction

import pw.forst.olb.core.predict.fitting.FinMathOptimization
import pw.forst.olb.core.predict.fitting.NoExtrapolation

fun main() {
    val inputFolder = "/home/lukas/repos/bp/job-data/test-input2"
    val outputFolder = "/home/lukas/repos/bp/job-data/fitted-out2"

    val predictionAlgorithms = listOf(
        FinMathOptimization(),
        NoExtrapolation(),
//        LstSqrs(),
        NoExtrapolation()
    )
    val simulations = PredictionSimulation()

    val results = InputProvider().readFolder(inputFolder)
//        .mapValues { (_, data) -> simulations.run(data, predictionAlgorithms) }
        .mapValues { (_, data) -> simulations.runlongerInterval(data, predictionAlgorithms) }

    OutputWritter().write(outputFolder, results)
}
