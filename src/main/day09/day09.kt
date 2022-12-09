package day09

import Point
import readInput

typealias Knot = Point

enum class Direction { R, U, D, L }

val regex = """([RUDL]) (\d+)""".toRegex()

val start = Knot(0, 0)

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

private fun List<Direction>.tailPositions(rope: Rope): MutableSet<Knot> {
    val touchedPositions = mutableSetOf<Knot>()
    var tmp = rope
    touchedPositions.add(rope.tail())
    forEach {
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

data class Rope(val knots: List<Knot> = emptyList()) {
    fun step(direction: Direction): Rope {
        var newRope = Rope().add(head().move(direction))
        knots.drop(1).forEach { knot ->
            val previousKnot = newRope.tail()
            newRope = if (knot.isAdjacentTo(previousKnot)) newRope.add(knot) else {
                val dx = knot.x - previousKnot.x
                val dy = knot.y - previousKnot.y
                val newX = if (dx < 0) knot.x + 1 else if (dx > 0) knot.x - 1 else knot.x
                val newY = if (dy < 0) knot.y + 1 else if (dy > 0) knot.y - 1 else knot.y
                newRope.add(Knot(newX, newY))
            }
        }
        return newRope
    }

    private fun head() = knots.first()
    fun tail(): Knot = knots.last()
    private fun add(knot: Knot) = copy(knots = knots + knot)
}

fun Knot.isAdjacentTo(other: Knot): Boolean {
    return other.x in (x - 1..x + 1) && other.y in (y - 1..y + 1)
}

fun Knot.move(direction: Direction) =
    when (direction) {
        Direction.R -> copy(x = x + 1)
        Direction.U -> copy(y = y + 1)
        Direction.D -> copy(y = y - 1)
        Direction.L -> copy(x = x - 1)
    }