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
private val goal = initialValley.filter { it.key.y == maxY }.keys.first { initialValley[it]!!.isEmpty() }
private val walls = initialValley.filter { it.value.firstOrNull() == WALL }
private val valleySequence = generateSequence(initialValley) { it.step() }.take(2000).toList()

private var minPathLength = 1_000

fun main() {

    println(part1())
    println(part2())
}

fun part1(): Int {


    return findPath(start)
//    return minPathLength
}

fun part2(): Int {

    val toGoal = findPath(start)
    val backToStart = findPath(start = goal, currentStep = toGoal, end = start)
    val backToGoalAgain = findPath(start = start, currentStep = backToStart)

    return backToGoalAgain
}

fun findPath(start: Point, currentStep: Int = 0, end: Point = goal): Int {
    val queue = mutableListOf(start to currentStep)
    val visited = mutableSetOf<Pair<Point, Int>>()

    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        if (current !in visited) {
            visited += current
            val nextValley = valleySequence[current.second + 1]
            val neighbours = validNeighboursFor(current.first)
            if (end in neighbours) return current.second + 1
            neighbours.filter { nextValley.isOpenAt(it) }.forEach {
                queue.add(it to current.second + 1)
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

fun Valley.isOpenAt(p: Point): Boolean {
    return this[p].isNullOrEmpty()
}

fun Valley.step(): Valley {
    // start and goal must always be in the map
    val result = mutableMapOf<Point, MutableList<Blizzard>>(
        start to mutableListOf(),
        goal to mutableListOf()
    )
    (minX..maxX).forEach { x ->
        (minY..maxY).forEach { y ->
            val here = Point(x, y)
            val blizzards = this[here]
            if (!blizzards.isNullOrEmpty()) {
                blizzards.forEach { blizzard ->
                    var newLocation = here + blizzard.diff
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

    return result
}

enum class Blizzard(val diff: Point) {
    LEFT(Point(-1, 0)),
    RIGHT(Point(1, 0)),
    UP(Point(0, -1)),
    DOWN(Point(0, 1)),
    WALL(Point(0, 0)),
}

fun Valley.print(p: Point) {
    (minY..maxY).forEach { y ->
        (minX..maxX).forEach { x ->
            if (p == Point(x, y)) {
                print("🤪")
            } else {
                val here = this[Point(x, y)]
                if (here.isNullOrEmpty())
                    print(" ")
                else if (here.size == 1)
                    print(
                        when (here.first()) {
                            LEFT -> "<"
                            RIGHT -> ">"
                            UP -> "^"
                            DOWN -> "v"
                            WALL -> "#"
                        }
                    )
                else print(here.size)
            }
        }
        println()
    }
    println()
}