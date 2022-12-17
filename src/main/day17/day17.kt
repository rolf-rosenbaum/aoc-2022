package day17

import Point
import day17.Direction.DOWN
import day17.Direction.LEFT
import day17.Direction.RIGHT
import readInput

const val leftWall = 0
const val rightWall = 6

typealias TetrisCave = MutableSet<Point>
typealias TetrisRock = Set<Point>

data class Jets(val jets: List<Direction>) {
    var index = 0
    fun next() = jets[index++ % jets.size]
}

enum class Direction {
    DOWN, LEFT, RIGHT
}

fun main() {
    val input = readInput("main/day17/Day17")
    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Long {
    return solve(input, 2022)
}

fun part2(input: List<String>): Long {
    return solve(input, 1000000000000L)
}

private fun solve(input: List<String>, numberOfRocks: Long): Long {
    FallingRock.reset()
    val cave = mutableSetOf(
        Point(0, 0),
        Point(1, 0),
        Point(2, 0),
        Point(3, 0),
        Point(4, 0),
        Point(5, 0),
        Point(6, 0),
    )

    val jets = input.first().parse()
    (1..90).forEach {
        var rock = cave.makeAppear(FallingRock.nextShape())
        do {
            rock = cave.moveRock(rock, jets.next()).first
            val result = cave.moveRock(rock, DOWN)
            rock = result.first
        } while (result.second)
        cave.addAll(rock)
    }

    val pattern = readInput("main/day17/pattern").map { it.toInt() }
    val patternSize = pattern.size
    val patternSum = pattern.sum()

    var rocksLeft = numberOfRocks - 90
    val foo = rocksLeft / patternSize * patternSum + pattern.take((rocksLeft - rocksLeft / patternSize * patternSize).toInt()).sum()

    return cave.height() + foo
}

private fun MutableSet<Point>.height() = maxOf { it.y }.toLong()

fun TetrisCave.moveRock(rock: TetrisRock, direction: Direction): Pair<TetrisRock, Boolean> {
    var movedRock: TetrisRock = rock
    var rockMoved = false
    when (direction) {
        DOWN -> {
            val tmp = rock.map { Point(it.x, it.y - 1) }
            if (tmp.none { this.contains(it) }) {
                movedRock = tmp.toSet()
                rockMoved = true
            }
        }

        LEFT -> {
            val tmp = rock.map { Point(it.x - 1, it.y) }
            if (tmp.none { this.contains(it) || it.x < leftWall }) {
                movedRock = tmp.toSet()
                rockMoved = true
            }
        }

        RIGHT -> {
            val tmp = rock.map { Point(it.x + 1, it.y) }
            if (tmp.none { this.contains(it) || it.x > rightWall }) {
                movedRock = tmp.toSet()
                rockMoved = true
            }
        }
    }
    return movedRock to rockMoved
}

fun TetrisCave.makeAppear(rock: FallingRock): TetrisRock {
    val xOffset = 2
    val yOffset = maxOf { it.y } + 4

    val newRock = rock.shape.map { Point(it.x + xOffset, it.y + yOffset) }.toSet()
    return newRock
}

fun TetrisCave.print() {
    (maxOf { it.y } downTo 0).forEach { y ->
        (-1..7).forEach { x ->
            if (y == 0) print("-")
            else
                if (x == -1 || x == 7) print("|")
                else
                    if (contains(Point(x, y))) print("#")
                    else print(".")
        }
        println()
    }
    println()
}

enum class FallingRock(val shape: TetrisRock) {
    LINE(setOf(Point(0, 0), Point(1, 0), Point(2, 0), Point(3, 0))),
    CROSS(setOf(Point(1, 0), Point(0, 1), Point(1, 1), Point(2, 1), Point(1, 2))),
    L_SHAPE(setOf(Point(0, 0), Point(1, 0), Point(2, 0), Point(2, 1), Point(2, 2))),
    NEEDLE(setOf(Point(0, 0), Point(0, 1), Point(0, 2), Point(0, 3))),
    BLOCK(setOf(Point(0, 0), Point(1, 0), Point(0, 1), Point(1, 1)));

    fun print() {
        (3 downTo 0).forEach { y ->
            (0..3).forEach { x ->
                if (shape.contains(Point(x, y))) print("#") else print(".")
            }
            println()
        }
        println()
    }

    companion object {
        private var index = 0
        fun nextShape(): FallingRock {
            return values()[index++ % 5]
        }

        fun reset() {
            index = 0
        }

    }
}

fun String.parse(): Jets = map {
    when (it) {
        '<' -> LEFT
        '>' -> RIGHT
        else -> error("illegal input")
    }
}.let { Jets(it) }

