import java.io.File
import kotlin.math.abs

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

fun <T> List<T>.second() = this[1]
fun <T, R> Pair<T, R>.reverse() = second to first

data class Point(val x: Int, val y: Int) {
    fun neighbours(includeCenter: Boolean = false): List<Point> =
        if (includeCenter) listOf(Point(x, y + 1), Point(x + 1, y), this, Point(x, y - 1), Point(x - 1, y))
        else listOf(Point(x, y + 1), Point(x + 1, y), Point(x, y - 1), Point(x - 1, y))

    fun allNeighbours(): Set<Point> =
        setOf(
            Point(x - 1, y - 1),
            Point(x, y - 1),
            Point(x + 1, y - 1),
            Point(x - 1, y),
            Point(x + 1, y),
            Point(x - 1, y + 1),
            Point(x, y + 1),
            Point(x + 1, y + 1)
        )


    fun distanceTo(other: Point) = abs(x - other.x) + abs(y - other.y)
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
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

fun List<Int>.findPattern(startIndex: Int = 1): Pair<Int, Int> {
    (startIndex..size / 2).forEach { windowSize ->
        print("$windowSize\r")
        val tmp = this.windowed(windowSize)
        tmp.forEachIndexed { index, intList ->
            if (index + windowSize >= tmp.size)
                return@forEachIndexed
            if (intList == tmp[index + windowSize])
                return index + 1 to windowSize
        }
    }
    error("no pattern")
}
