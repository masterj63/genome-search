import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.io.File
import java.nio.file.Files
import java.util.concurrent.Executors
import kotlin.system.measureTimeMillis


class Main

const val needle = "wax synthase"

fun main() {
    val elapsedMillis = measureTimeMillis {
        processRootDir()
    }
    println("$elapsedMillis millis elapsed")
}

private fun processRootDir(rootDir: String = "../genomas") {
    val result = Executors.newFixedThreadPool(8)
        .asCoroutineDispatcher()
        .use { dispatcher ->
            val functions: List<suspend () -> List<String>> =
                File(rootDir).walk()
                    .filter(File::isFile)
                    .map { file ->
                        suspend {
                            val result = mutableListOf<String>()
                            val text = Files.readString(file.toPath())
                            var pos = 0
                            while (pos < text.length) {
                                val nextPos = text.indexOf(needle, pos)
                                if (nextPos == -1) break
                                val lPos = run {
                                    var i = nextPos
                                    while (i > 0 && text[i] != '\n') i--
                                    return@run i
                                }
                                val rPos = text.indexOf('\n', nextPos + needle.length)
                                    .let { if (it == -1) text.length else it }
                                result.add(text.substring(lPos, rPos))
                                pos = 1 + rPos
                            }
                            result
                        }
                    }
                    .toList()
            runBlocking {
                functions
                    .map { fn ->
                        async(dispatcher) { fn() }
                    }
                    .flatMap { deferred ->
                        try {
                            deferred.await()
                        } catch (e: Exception) {
                            println(e)
                            listOf<String>()
                        }
                    }
            }
        }
    println(result.size)
}

