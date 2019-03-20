package pw.forst.olb.simulation.prediction

import mu.KLogging
import org.koin.ext.isInt
import java.io.File

class InputProvider {

    private companion object : KLogging()

    fun readFolder(folder: String): Map<String, List<Input>> = File(folder).listFiles().associate { it.name to readFile(it) }

    fun readFile(file: File): List<Input> = runCatching {
        file
            .bufferedReader()
            .use { reader ->
                reader.readLines().mapNotNull { line ->
                    line.split(";")
                        .let {
                            if (!it[2].isInt()) null
                            else
                                Input(
                                    index = it[0].toLong(),
                                    timeRecord = it[1].toLong(),
                                    iterationLength = it[2].toInt(),
                                    cost = it[3].toDouble()
                                )
                        }
                }
            }.toList()
    }
        .onFailure { logger.error(it) { "error while loading input" } }
        .getOrElse { emptyList() }
}
