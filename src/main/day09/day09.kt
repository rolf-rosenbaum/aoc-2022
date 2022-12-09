package day09

import Point
import readInput

val regex = """([RUDL]) (\d+)""".toRegex()

enum class Direction {
    R, U, D, L
}

val start = Point(0, 0)

fun main() {
    val input = readInput("main/day09/Day09")

    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Int {
    return input.toSteps().tailPositions(Rope(listOf(start, start))).size
}

fun part2(input: List<String>): Int {

    return input.toSteps()
        .tailPositions(Rope(listOf(start, start, start, start, start, start, start, start, start, start))).size
}

private fun List<Direction>.tailPositions(rope: Rope): MutableSet<Point> {
    val touchedPositions = mutableSetOf<Point>()
    var tmp = rope
    touchedPositions.add(rope.tail())
    map {
        tmp = tmp.step(it)
        touchedPositions.add(tmp.tail())
    }
    return touchedPositions
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

data class Rope(val points: List<Point> = emptyList()) {
    fun step(direction: Direction): Rope {
        var newRope = Rope().add(head().move(direction))
        points.drop(1).forEach {
            val newHead = newRope.tail()
            newRope = if (it.isAdjacentTo(newHead)) {
                newRope.add(it)
            } else {
                val dx = it.x - newHead.x
                val dy = it.y - newHead.y
                val newX = if (dx < 0) it.x + 1 else if (dx > 0) it.x - 1 else it.x
                val newY = if (dy < 0) it.y + 1 else if (dy > 0) it.y - 1 else it.y
                newRope.add(Point(newX, newY))
            }
        }
        return newRope
    }

    private fun head() = points.first()
    fun tail(): Point = points.last()
    private fun add(point: Point) = copy(points = points + point)
}

fun Point.isAdjacentTo(other: Point): Boolean {
    return other.x in (x - 1..x + 1) && other.y in (y - 1..y + 1)
}

fun Point.move(direction: Direction) =
    when (direction) {
        Direction.R -> copy(x = x + 1)
        Direction.U -> copy(y = y + 1)
        Direction.D -> copy(y = y - 1)
        Direction.L -> copy(x = x - 1)
    }