package pw.forst.olb.simulation.input.data

import java.io.File


class DataParser {

    private fun read(fileName: String): AlgorithmRuntimeInfo =
        AlgorithmRuntimeInfo(
            name = fileName,
            data = readData(fileName)
        )

    private fun readData(fileName: String): Collection<AlgorithmRunData> =
        File(fileName).bufferedReader().useLines {
            val iterator = it.iterator()
            if (!iterator.hasNext()) return emptyList()
            else {
                val next = iterator.next()
                if (next != "Index;Time point [ms];Iteration length [ms];Cost") throw IllegalStateException("Wrong format! $next")
            }
            val resultList = mutableListOf<AlgorithmRunData>()
            iterator.forEachRemaining { line ->
                val spited = line.split(";")
                resultList.add(
                    AlgorithmRunData(
                        index = spited[0].toInt(),
                        timePoint = spited[1].toInt(),
                        iterationLength = spited[2].toInt(),
                        cost = spited[3].toDouble()
                    )
                )
            }
            resultList
        }


    fun readFolder(folder: String): Collection<AlgorithmRuntimeInfo> = File(folder).listFiles().filter { it.isFile }.map { read("$folder/${it.name}") }
}
