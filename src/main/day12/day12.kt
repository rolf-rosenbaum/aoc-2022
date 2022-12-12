package day12

import Point
import readInput

typealias Grid = MutableMap<Point, Char>

fun main() {
    val input = readInput("main/day12/Day12")

    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Int = input.solveFor('S')

fun part2(input: List<String>): Int = input.solveFor('a')

fun List<String>.solveFor(start: Char): Int {
    val grid = parse()

    val startPoint = grid.filter { it.value == 'S' }.keys.first()
    val goal = grid.filterValues { it == 'E' }.keys.first()

    grid[goal] = 'z'

    return grid.filter { it.value == start }.mapNotNull {
        grid[startPoint] = 'a'
        distance(it.key, goal, grid)
    }.minOf { it }
}

private fun distance(start: Point, goal: Point, grid: Grid): Int? {
    // the metaphor is to "flood" the terrain with water, always going in all 4 directions if the neighbouring point is not too high
    // and keep track of the distance. 
    // As soon as the goal is "wet", we have our result

    val lake = mutableMapOf(start to 0)
    var distance = 0
    var currentSize = 0
    do {
        currentSize = lake.size
        distance = grid.floodValley(lake, distance)
    } while (!lake.containsKey(goal) && lake.size > currentSize)

    if (lake.size <= currentSize) {
        // there is no valid path from start to goal 
        return null
    }
    return distance
}

fun Grid.floodValley(lake: MutableMap<Point, Int>, distance: Int): Int {
    lake.filterValues { it == distance }.forEach { (point, _) ->
        point.neighbours().filter {
            this[it] != null && this[it]!! - this[point]!! <= 1
        }.forEach {
            if (!lake.containsKey(it)) lake[it] = distance + 1
        }
    }
    return distance + 1
}

fun List<String>.parse(): Grid {
    val result = mutableMapOf<Point, Char>()
    forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            result[Point(x, y)] = c
        }
    }
    return result
}
