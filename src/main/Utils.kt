import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.abs

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt")
    .readLines()


fun <T> List<T>.second() = this[1]
fun <T, R> Pair<T, R>.reverse() = second to first

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

data class Point(val x: Int, val y: Int) {
    fun neighbours(): List<Point> = listOf(Point(x + 1, y), Point(x, y + 1), Point(x - 1, y), Point(x, y - 1))
    fun distanceTo(other: Point) = abs(x - other.x) + abs(y - other.y)
}

fun IntRange.fullyContains(other: IntRange) =
    contains(other.first) && contains(other.last)

fun IntRange.overlapsWith(other: IntRange) =
    contains(other.first) || contains(other.last)

fun IntRange.union(other: IntRange): IntRange? {
    return if (overlapsWith(other))
        IntRange(minOf(first, other.first), maxOf(last, other.last))
    else null
}
