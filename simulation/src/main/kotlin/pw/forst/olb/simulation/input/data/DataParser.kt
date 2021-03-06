package pw.forst.olb.simulation.input.data

import pw.forst.olb.common.dto.TimeImpl
import java.io.File
import java.util.concurrent.TimeUnit


class DataParser {

    private fun read(fileName: String, name: String? = null): AlgorithmRuntimeInfo =
        AlgorithmRuntimeInfo(
            name = name ?: fileName,
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
                        timePoint = TimeImpl(position = spited[1].toLong(), units = TimeUnit.MILLISECONDS),
                        iterationLength = spited[2].toInt(),
                        cost = spited[3].toDouble()
                    )
                )
            }
            resultList
        }


    fun readFolder(folder: String): Collection<AlgorithmRuntimeInfo> =
        File(folder).listFiles().filter { it.isFile }.sortedBy { it.name }.mapIndexed { idx, it -> read(it.absolutePath, idx.toString()) }
}
