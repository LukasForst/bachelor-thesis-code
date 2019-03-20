package pw.forst.olb.simulation.prediction

import mu.KLogging
import java.io.File

class OutputWritter {

    private companion object : KLogging()

    fun write(folder: String, data: Map<String, List<Output>>): Boolean =
        runCatching {
            with(File(folder)) { if (!exists()) mkdirs() }
            data.map { (fileName, data) -> write(File("$folder${File.separator}$fileName"), data) }
        }
            .onFailure { logger.error(it) { "error while witting output" } }
            .getOrDefault(listOf(false)).all { it }


    fun write(file: File, outputs: List<Output>) = runCatching {
        file.bufferedWriter().use { writer ->
            with(writer) {
                val predictions = (0..outputs.first().costPredictions.size).joinToString(";")
                write("Index;Time point [ms];Iteration length [ms];Cost;$predictions")
                newLine()
                write(outputs.joinToString("\n") { it.toCsv() })
                newLine()
            }
        }
    }.onFailure { logger.error(it) { "error while witting output" } }
        .isSuccess

    private fun Output.toCsv() = "$index;$timeRecord;$iterationLength;$cost;${costPredictions.joinToString(";")}"
}
