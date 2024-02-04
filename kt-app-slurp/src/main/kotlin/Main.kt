import java.io.File
import java.nio.file.Files
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
    val result = File(rootDir).walk()
        .filter(File::isFile)
        .flatMap { processFile(it) }
        .toList()
    println(result.size)
}

fun processFile(file: File): List<String> {
    val result= mutableListOf<String>()
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
    return result
}
