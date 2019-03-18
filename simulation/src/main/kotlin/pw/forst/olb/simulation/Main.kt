import pw.forst.olb.core.predict.fitting.NonLinearHyperbolaRegression
import java.io.File

fun main() {
    val results = mutableMapOf<Long, Long>()

    val fit = NonLinearHyperbolaRegression()

    File("/tmp/bp/testing/main2.txt")
        .bufferedWriter()
        .use {
            it.write(
                results
                    .map { (key, value) -> "$key;$value" }
                    .joinToString("\n")
            )
        }
}
