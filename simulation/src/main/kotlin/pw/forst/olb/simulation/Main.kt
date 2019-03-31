import java.io.File

fun main() {
    val results = mutableMapOf<Long, Long>()

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
