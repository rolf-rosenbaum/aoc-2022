package day24

import Point
import day24.Blizzard.DOWN
import day24.Blizzard.LEFT
import day24.Blizzard.RIGHT
import day24.Blizzard.UP
import day24.Blizzard.WALL
import readInput

typealias Valley = Map<Point, List<Blizzard>>

private val initialValley = readInput("main/day24/Day24").toValley()
private val maxX = initialValley.keys.maxOf { it.x }
private val minX = initialValley.keys.minOf { it.x }
private val maxY = initialValley.keys.maxOf { it.y }
private val minY = initialValley.keys.minOf { it.y }
private val start = initialValley.filter { it.key.y == minY }.keys.first { initialValley[it]!!.isEmpty() }
private val exit = initialValley.filter { it.key.y == maxY }.keys.first { initialValley[it]!!.isEmpty() }
private val walls = initialValley.filter { it.value.firstOrNull() == WALL }
private val valleySequence = generateSequence(initialValley) { it.step() }.take(1000).toList()

fun main() {

    println(part1())
    println(part2())
}

fun part1(): Int {
    return findPath()
}

fun part2(): Int {
    val toGoal = findPath()
    val backToStart = findPath(entry = exit, currentStep = toGoal, goal = start)
    return findPath(currentStep = backToStart)
}

fun findPath(entry: Point = start, currentStep: Int = 0, goal: Point = exit): Int {
    val pathsToCheck = mutableListOf(State(entry, currentStep))
    val checked = mutableSetOf<State>()

    while (pathsToCheck.isNotEmpty()) {
        val current = pathsToCheck.removeFirst()
        if (current !in checked) {
            val nextValley = valleySequence[current.step + 1]
            val neighbours = validNeighboursFor(current.point).filter { nextValley.isOpenAt(it) }

            if (goal in neighbours) return current.step + 1

            checked += current
            neighbours.forEach {
                pathsToCheck.add(State(it, current.step + 1))
            }
        }
    }
    error("lost in the vally of blizzards")
}

fun List<String>.toValley(): Valley {
    val valley = mutableMapOf<Point, List<Blizzard>>()
    mapIndexed { y, line ->
        line.mapIndexed { x, c ->
            val p = Point(x, y)
            when (c) {
                '^' -> valley[p] = listOf(UP)
                'v' -> valley[p] = listOf(DOWN)
                '<' -> valley[p] = listOf(LEFT)
                '>' -> valley[p] = listOf(RIGHT)
                '#' -> valley[p] = listOf(WALL)
                else -> valley[p] = emptyList()
            }
        }
    }
    return valley
}

fun validNeighboursFor(p: Point) = p.neighbours(true)
    .filterNot { it in walls }
    .filter { it.x in (minX..maxX) }
    .filter { it.y in (minY..maxY) }

fun Valley.isOpenAt(p: Point): Boolean = this[p].isNullOrEmpty()

fun Valley.step(): Valley =
    mutableMapOf<Point, MutableList<Blizzard>>(
        // start and goal must always be in the map
        start to mutableListOf(),
        exit to mutableListOf()
    ).let { result ->
        (minX..maxX).forEach { x ->
            (minY..maxY).forEach { y ->
                val here = Point(x, y)
                val blizzards = this[here]
                if (!blizzards.isNullOrEmpty()) {
                    blizzards.forEach { blizzard ->
                        var newLocation = here + blizzard.offset
                        if (newLocation in walls) {
                            newLocation = when (blizzard) {
                                LEFT -> Point(maxX - 1, y)
                                RIGHT -> Point(minX + 1, y)
                                UP -> Point(x, maxY - 1)
                                DOWN -> Point(x, minY + 1)
                                WALL -> Point(x, y) // walls do not move
                            }
                        }
                        if (result[newLocation] == null) result[newLocation] = mutableListOf(blizzard)
                        else result[newLocation]!!.add(blizzard)
                    }
                }
            }
        }
        result
    }

enum class Blizzard(val offset: Point) {
    LEFT(Point(-1, 0)),
    RIGHT(Point(1, 0)),
    UP(Point(0, -1)),
    DOWN(Point(0, 1)),
    WALL(Point(0, 0)),
}

data class State(val point: Point, val step: Int)