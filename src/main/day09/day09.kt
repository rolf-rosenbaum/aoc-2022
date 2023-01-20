package day09

import Point
import kotlin.math.sign
import readInput

typealias Segment = Point

enum class Direction { R, U, D, L }

val regex = """([RUDL]) (\d+)""".toRegex()

val start = Segment(0, 0)

fun main() {
    val input = readInput("main/day09/Day09")

    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Int {
    return input.toSteps().tailPositions(2).size
}

fun part2(input: List<String>): Int {
    return input.toSteps().tailPositions(10).size
}

private fun List<Direction>.tailPositions(length: Int) =
    tailPositions(Rope(Array(length) { Point(0, 0) }.toList()))

private fun List<Direction>.tailPositions(rope: Rope) = iterator().let { directions ->
    generateSequence(rope) {
        if (directions.hasNext()) it.step(directions.next())
        else null
    }.takeWhile { it != null }.map { it.tail() }.distinct().toList()
}

fun List<String>.toSteps(): List<Direction> {
    val directions = mutableListOf<Direction>()
    forEach {
        regex.matchEntire(it)?.destructured?.let { (direction, steps) ->
            repeat(steps.toInt()) {
                directions.add(Direction.valueOf(direction))
            }
        } ?: error("invalid input")
    }
    return directions
}

data class Rope(val segments: List<Segment> = emptyList()) {
    fun step(direction: Direction): Rope {
        var newRope = Rope().add(head().move(direction))
        segments.drop(1).forEach { segment ->
            val head = newRope.tail()
            newRope =
                if (segment.isAdjacentTo(head)) newRope.add(segment)
                else newRope.add(Segment((head.x - segment.x).sign + segment.x, (head.y - segment.y).sign + segment.y))
        }
        return newRope
    }

    private fun head() = segments.first()
    fun tail(): Segment = segments.last()
    private fun add(segment: Segment) = copy(segments = segments + segment)
}

fun Segment.isAdjacentTo(other: Segment): Boolean {
    return other.x in (x - 1..x + 1) && other.y in (y - 1..y + 1)
}

fun Segment.move(direction: Direction) =
    when (direction) {
        Direction.R -> copy(x = x + 1)
        Direction.U -> copy(y = y + 1)
        Direction.D -> copy(y = y - 1)
        Direction.L -> copy(x = x - 1)
    }