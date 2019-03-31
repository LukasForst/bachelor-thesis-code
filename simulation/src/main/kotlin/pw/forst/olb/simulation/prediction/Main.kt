package pw.forst.olb.simulation.prediction

import pw.forst.olb.core.predict.fitting.HyperbolaPredictionBuilder
import pw.forst.olb.core.predict.fitting.NoExtrapolation

fun main() {
    val inputFolder = "/home/lukas/repos/bp/job-data/test-input2"
    val outputFolder = "/home/lukas/repos/bp/job-data/fitted-out2"

    val builder = HyperbolaPredictionBuilder.create()
//        .setDataPreprocessor { it.reduceDistribution() }

    val predictionAlgorithms = listOf(
        builder.buildWithFinMath(),
        builder.buildWithApache(),
        NoExtrapolation()
    )
    val simulations = PredictionSimulation()

    val results = InputProvider().readFolder(inputFolder)
//        .mapValues { (_, data) -> simulations.run(data, predictionAlgorithms) }
        .mapValues { (_, data) -> simulations.runlongerInterval(data, predictionAlgorithms) }

    OutputWritter().write(outputFolder, results)
}
