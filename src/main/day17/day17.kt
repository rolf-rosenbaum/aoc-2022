package day17

import Point
import day17.Direction.DOWN
import day17.Direction.LEFT
import day17.Direction.RIGHT
import findPattern
import readInput
import second

const val leftWall = 0
const val rightWall = 6

typealias TetrisCave = MutableSet<Point>
typealias TetrisRock = Set<Point>

data class Jets(val jets: List<Direction>) {
    private var index = 0
    fun next() = jets[index++ % jets.size]
}

enum class Direction {
    DOWN, LEFT, RIGHT
}

val fallingRocks = listOf(
    setOf(Point(0, 0), Point(1, 0), Point(2, 0), Point(3, 0)),
    setOf(Point(1, 0), Point(0, 1), Point(1, 1), Point(2, 1), Point(1, 2)),
    setOf(Point(0, 0), Point(1, 0), Point(2, 0), Point(2, 1), Point(2, 2)),
    setOf(Point(0, 0), Point(0, 1), Point(0, 2), Point(0, 3)),
    setOf(Point(0, 0), Point(1, 0), Point(0, 1), Point(1, 1))
)

fun main() {
    val input = readInput("main/day17/Day17")
    val heightDiffs = heightDiffs(input)
    val (rocksBeforePattern, patternSize) = heightDiffs.findPattern(1650)
    println("$rocksBeforePattern, $patternSize")

    println("Part1: ${solve(heightDiffs, rocksBeforePattern, patternSize, 2022)}")
    println("Part1: ${solve(heightDiffs, rocksBeforePattern, patternSize, 1000000000000L)}")
}

private fun solve(heightDiffs: List<Int>, rocksBeforePattern: Int, patternSize: Int, numberOfRocks: Long): Long {
    val rocksLeft = numberOfRocks - rocksBeforePattern

    val pattern = heightDiffs.drop(rocksBeforePattern - 1).take(patternSize)
    val patternSum = pattern.sum()

    return heightDiffs.take(rocksBeforePattern).sum() +
            (rocksLeft / patternSize * patternSum +
                    pattern.take((rocksLeft - rocksLeft / patternSize * patternSize).toInt()).sum())
}

fun heightDiffs(input: List<String>): List<Int> {
    val cave = emptyCaveWithFloor()
    val jets = input.first().parse()
    return (0..5000).map {
        var rock = cave.nextRock(shapeFor(it))
        do {
            rock = cave.moveRock(rock, jets.next()).first
            val result = cave.moveRock(rock, DOWN)
            rock = result.first
        } while (result.second)
        cave.addAll(rock)
        cave.height()
    }.windowed(2).map { (it.second() - it.first()).toInt() }
}

private fun shapeFor(it: Int) = fallingRocks[it % fallingRocks.size]

private fun emptyCaveWithFloor() = mutableSetOf(
    Point(0, 0),
    Point(1, 0),
    Point(2, 0),
    Point(3, 0),
    Point(4, 0),
    Point(5, 0),
    Point(6, 0),
)

private fun TetrisCave.height() = maxOf { it.y }.toLong()

fun TetrisCave.moveRock(rock: TetrisRock, direction: Direction): Pair<TetrisRock, Boolean> {
    var movedRock: TetrisRock = rock
    var rockMoved = false
    when (direction) {
        DOWN -> {
            val probedRock = rock.map { Point(it.x, it.y - 1) }
            if (probedRock.none { this.contains(it) }) {
                movedRock = probedRock.toSet()
                rockMoved = true
            }
        }

        LEFT -> {
            val probedRock = rock.map { Point(it.x - 1, it.y) }
            if (probedRock.none { this.contains(it) || it.x < leftWall }) {
                movedRock = probedRock.toSet()
                rockMoved = true
            }
        }

        RIGHT -> {
            val probedRock = rock.map { Point(it.x + 1, it.y) }
            if (probedRock.none { this.contains(it) || it.x > rightWall }) {
                movedRock = probedRock.toSet()
                rockMoved = true
            }
        }
    }
    return movedRock to rockMoved
}

fun TetrisCave.nextRock(rock: TetrisRock): TetrisRock {
    val xOffset = 2
    val yOffset = maxOf { it.y } + 4
    return rock.map { Point(it.x + xOffset, it.y + yOffset) }.toSet()
}

fun String.parse(): Jets = map {
    when (it) {
        '<' -> LEFT
        '>' -> RIGHT
        else -> error("illegal input")
    }
}.let { Jets(it) }

