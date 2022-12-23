package day22

import Point
import day22.Direction.DOWN
import day22.Direction.LEFT
import day22.Direction.RIGHT
import day22.Direction.UP
import readInput

typealias Board = Map<Point, Char>

const val WALL = '#'
const val OPEN = '.'

fun main() {
    val input = readInput("main/day22/Day22")

    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Int {
    val board = input.parseBoard()
    val directions = input.readDirections()

    var currentDirection = RIGHT
    var position = board.filter { it.key.y == 1 }.minBy { it.key.x }.key

    var index = 0
    while (index < directions.length) {
        var steps = ""
        while (index < directions.length && directions[index].isDigit()) {
            steps += directions[index]
            index++
        }
        val numSteps = steps.toInt()
        repeat(numSteps) {
            position = board.nextTileFrom(position, currentDirection)
        }
        if (index < directions.length) {
            currentDirection = if (directions[index] == 'L') currentDirection.left() else currentDirection.right()
            index++
        }
    }



    return position.y * 1000 + position.x * 4 + currentDirection.facing
}

fun part2(input: List<String>): Int {
    val board = input.parseBoard()
    val directions = input.readDirections()

    var currentDirection = RIGHT
    var position = board.filter { it.key.y == 1 }.minBy { it.key.x }.key

    var index = 0
    while (index < directions.length) {
        var steps = ""
        while (index < directions.length && directions[index].isDigit()) {
            steps += directions[index]
            index++
        }
        val numSteps = steps.toInt()
        repeat(numSteps) {
            val (pos, dir) = board.nextTileOnCubeFrom(position, currentDirection)
            position = pos
            currentDirection = dir
        }
        if (index < directions.length) {
            currentDirection = if (directions[index] == 'L') currentDirection.left() else currentDirection.right()
            index++
        }
    }

    return position.y * 1000 + position.x * 4 + currentDirection.facing
}

fun List<String>.parseBoard(): Board {
    val board = mutableMapOf<Point, Char>()
    this.takeWhile { it.isNotBlank() }.mapIndexed { y, line ->
        line.mapIndexed { x, c ->
            if (c == OPEN || c == WALL) board[Point(x + 1, y + 1)] = c
        }
    }
    return board
}

fun List<String>.readDirections() = this.dropLastWhile { it.isBlank() }.last()


fun Board.nextTileFrom(p: Point, direction: Direction): Point = when (direction) {
    UP -> {
        var next = Point(p.x, p.y - 1)
        if (this[next] == WALL) next = p
        if (this[next] == null) next = keys.filter { it.x == p.x }.maxBy { it.y }
        if (this[next] == WALL) next = p
        next
    }

    DOWN -> {
        var next = Point(p.x, p.y + 1)
        if (this[next] == WALL) next = p
        if (this[next] == null) next = keys.filter { it.x == p.x }.minBy { it.y }
        if (this[next] == WALL) next = p
        next
    }

    RIGHT -> {
        var next = Point(p.x + 1, p.y)
        if (this[next] == WALL) next = p
        if (this[next] == null) next = keys.filter { it.y == p.y }.minBy { it.x }
        if (this[next] == WALL) next = p
        next
    }

    LEFT -> {
        var next = Point(p.x - 1, p.y)
        if (this[next] == WALL) next = p
        if (this[next] == null) next = keys.filter { it.y == p.y }.maxBy { it.x }
        if (this[next] == WALL) next = p
        next
    }
}

fun Board.nextTileOnCubeFrom(p: Point, direction: Direction): Pair<Point, Direction> {
    return when (direction) {
        UP -> nextPositionAndDirection(Point(p.x, p.y - 1), p, direction)
        DOWN -> nextPositionAndDirection(Point(p.x, p.y + 1), p, direction)
        RIGHT -> nextPositionAndDirection(Point(p.x + 1, p.y), p, direction)
        LEFT -> nextPositionAndDirection(Point(p.x - 1, p.y), p, direction)
    }
}

private fun Board.nextPositionAndDirection(next: Point, p: Point, direction: Direction): Pair<Point, Direction> {
    var next1 = next
    var newDirection = direction
    if (this[next1] == WALL) next1 = p
    if (this[next1] == null) {
        val (pos, dir) = gotoNextSide(p, direction)
        next1 = pos
        newDirection = dir
    }
    if (this[next1] == WALL) {
        next1 = p
        newDirection = direction
    }
    return next1 to newDirection
}

private fun sideOf(pos: Point): Char {

    if (pos.x in 51..100 && pos.y in 1..50) return 'A'
    if (pos.x in 101..150 && pos.y in 1..50) return 'B'
    if (pos.x in 51..100 && pos.y in 51..100) return 'C'
    if (pos.x in 51..100 && pos.y in 101..150) return 'D'
    if (pos.x in 1..50 && pos.y in 101..150) return 'E'
    if (pos.x in 1..50 && pos.y in 151..200) return 'F'
    error("Side does not exist for $pos")
}

fun gotoNextSide(position: Point, direction: Direction): Pair<Point, Direction> {
    var nextDir = direction
    val side = sideOf(position)
    var nextPos = position
    val sideLength = 50
    if (side == 'A' && direction == UP) {
        nextDir = RIGHT
        nextPos = Point(1, 3 * sideLength + position.x - sideLength) // nextSide = F
    } else if (side == 'A' && direction == LEFT) {
        nextDir = RIGHT
        nextPos = Point(1, 2 * sideLength + (sideLength - position.y)) // nextSide = E
    } else if (side == 'B' && direction == UP) {
        nextDir = UP
        nextPos = Point(position.x - 2 * sideLength, 201) // nextSide = F
    } else if (side == 'B' && direction == RIGHT) {
        nextDir = LEFT
        nextPos = Point(101, (sideLength - position.y) + 2 * sideLength) // nextSide = D
    } else if (side == 'B' && direction == DOWN) {
        nextDir = LEFT
        nextPos = Point(101, sideLength + (position.x - 2 * sideLength)) // nextSide = C
    } else if (side == 'C' && direction == RIGHT) {
        nextDir = UP
        nextPos = Point((position.y - sideLength) + 2 * sideLength, sideLength) // nextSide = B
    } else if (side == 'C' && direction == LEFT) {
        nextDir = DOWN
        nextPos = Point(position.y - sideLength, 101) // nextSide = E
    } else if (side == 'E' && direction == LEFT) {
        nextDir = RIGHT
        nextPos = Point(51, sideLength - (position.y - 2 * sideLength)) // nextSide = 'A'
    } else if (side == 'E' && direction == UP) {
        nextDir = RIGHT
        nextPos = Point(51, sideLength + position.x) // nextSide = C
    } else if (side == 'D' && direction == DOWN) {
        nextDir = LEFT
        nextPos = Point(101, 3 * sideLength + (position.x - sideLength)) // nextSide = F
    } else if (side == 'D' && direction == RIGHT) {
        nextDir = LEFT
        nextPos = Point(151, sideLength - (position.y - sideLength * 2)) // nextSide = B
    } else if (side == 'F' && direction == RIGHT) {
        nextDir = UP
        nextPos = Point((position.y - 3 * sideLength) + sideLength, 151) // nextSide = D
    } else if (side == 'F' && direction == LEFT) {
        nextDir = DOWN
        nextPos = Point(sideLength + (position.y - 3 * sideLength), 1) // nextSide = 'A'
    } else if (side == 'F' && direction == DOWN) {
        nextDir = DOWN
        nextPos = Point(position.x + 2 * sideLength, 1) // nextSide = B
    }
    return nextPos to nextDir
}

enum class Direction(val facing: Int) {
    UP(3), DOWN(1), RIGHT(0), LEFT(2);

    fun right() = when (this) {
        UP -> RIGHT
        DOWN -> LEFT
        RIGHT -> DOWN
        LEFT -> UP
    }

    fun left() = when (this) {
        UP -> LEFT
        DOWN -> RIGHT
        RIGHT -> UP
        LEFT -> DOWN
    }
}
