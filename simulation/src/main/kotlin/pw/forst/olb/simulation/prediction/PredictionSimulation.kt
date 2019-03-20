package pw.forst.olb.simulation.prediction

import pw.forst.olb.core.predict.fitting.Prediction

class PredictionSimulation {
    fun run(input: List<Input>, alg: List<Prediction>): List<Output> {
        val prediction = mutableListOf<Output>()

        prediction.add(input[0].toOutput((0..alg.size).map { input[0].cost }.toList()))
        for (i in 1 until input.size) {
            val previousData = input.subList(0, i - 1).associate { it.index.toDouble() to it.cost }
            val predicted = alg.map { it.predict(previousData, input[i].index.toDouble()) ?: 0.0 }

            prediction.add(input[i].toOutput(predicted))
        }
        return prediction
    }

    private fun Input.toOutput(predicted: List<Double>): Output = Output(
        index = index,
        timeRecord = timeRecord,
        iterationLength = iterationLength,
        cost = cost,
        costPredictions = predicted
    )
}
