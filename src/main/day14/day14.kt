package day14

import kotlin.math.max
import kotlin.math.min
import readInput

typealias Cave = MutableSet<Point>
typealias Rock = Point
typealias Grain = Point

data class Point(val x: Int, val y: Int)

val source = Point(500, 0)

fun main() {
    val input = readInput("main/day14/Day14_test")
    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Int {

    val cave = input.parseTosSolidRock()
    return cave.pourSand()
}

fun part2(input: List<String>): Int {
    val cave = input.parseTosSolidRock()
    val maxX = cave.maxOf { it.x } //.also { println("max x: $it") }
    val minX = cave.minOf { it.x } //.also { println("min x: $it") }

    val floorY = cave.maxOf { it.y } + 2
    cave.addRock(Point(minX - 1500, floorY), Point(maxX + 1500, floorY))

    return cave.pourSand()
}

fun Cave.pourSand(): Int {
    val startSize = size
    do {
        val droppedGrain = source.fall(this)
        if (droppedGrain != null) add(droppedGrain)
        if (size % 1000 == 0) println(size)
    } while (droppedGrain != null)
    return size - startSize
}

private fun Grain.fall(cave: Cave): Grain? {
    if (cave.contains(source))
        return null

    if (down().y > cave.maxOf { it.y }) return null

    if (!cave.contains(down())) return down().fall(cave)
    if (!cave.contains(downLeft())) return downLeft().fall(cave)
    if (!cave.contains(downRight())) return downRight().fall(cave)

    return this
}

fun Cave.addRock(start: Point, end: Point) {
    val startX = min(start.x, end.x)
    val startY = min(start.y, end.y)
    val endX = max(start.x, end.x)
    val endY = max(start.y, end.y)

    (startX..endX).forEach { x ->
        (startY..endY).forEach { y ->
            add(Rock(x, y))
        }
    }
}

fun List<String>.parseTosSolidRock(): Cave {
    val cave = mutableSetOf<Point>()

    map { line ->
        line.split(" -> ")
            .map { it.split(", ") }
            .windowed(2) { (start, end) ->
                cave.addRock(
                    rock(start.first()),
                    rock(end.first())
                )
            }
    }
    return cave
}

fun rock(point: String): Rock {
    return point.split(",").map { it.toInt() }.let { (x, y) ->
        Point(x, y)
    }
}

private fun Grain.down() = copy(y = y + 1)
private fun Grain.downLeft() = copy(x = x - 1, y = y + 1)
private fun Grain.downRight() = copy(x = x + 1, y = y + 1)

