import java.io.File
import java.io.RandomAccessFile
import java.nio.channels.FileChannel
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
    val acc = mutableListOf<String>()
    RandomAccessFile(file, "r").channel.use { fc ->
        val mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size())
        val line = StringBuilder()
        for (i in 0 until mbb.limit()) {
            val c = Char(mbb.get().toUShort())
            if (c == '\n') {
                if (line.contains(needle)) {
                    acc.add(line.toString())
                }
                line.setLength(0)
            } else {
                line.append(c)
            }
        }
        if (line.contains(needle)) {
            acc.add(line.toString())
        }
        mbb.clear()
    }
    return acc
}
