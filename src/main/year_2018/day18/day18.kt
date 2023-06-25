package year_2018.day18

import Point
import findPattern
import readInput

typealias Forest = Map<Point, Char>

fun main() {
    val input = readInput("main/year_2018/day18/Day18")
    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Int {
    val forest = input.parse()
    return forest.resourceValueList(11).last()
}

fun part2(input: List<String>): Int {
    val stepCount = 1000000000
    val forest = input.parse()
    val resourceValueList = forest.resourceValueList()
    val (stepsBeforePattern, patternSize) = resourceValueList.findPattern(1)

    val indexInCycle = (stepCount - stepsBeforePattern) % patternSize
    return resourceValueList.drop(stepsBeforePattern)[indexInCycle]
}

fun Forest.step(): Forest {
    return this.keys.associateWith { here ->

        when (this[here]) {
            '.' -> if (this.countNeighborsOfKind(here, '|') >= 3) '|' else '.'
            '|' -> if (this.countNeighborsOfKind(here, '#') >= 3) '#' else '|'
            '#' -> if (countNeighborsOfKind(here, '#') > 0 && countNeighborsOfKind(here, '|') > 0) '#' else '.'
            else -> error("Something went wrong: $here, ${this[here]}")
        }
    }
}

private fun Forest.resourceValue(): Int = count { it.value == '#' } * count { it.value == '|' }

fun Forest.resourceValueList(n: Int = 600): List<Int> = generateSequence(this) { it.step() }.take(n).map { it.resourceValue() }.toList()

private fun Forest.prettyPrint() {
    (0..49).forEach { y ->
        (0..49).forEach { x ->
            if (this[Point(x, y)] != null) print(this[Point(x, y)])
        }
        println()
    }
    println()
}

fun Forest.countNeighborsOfKind(p: Point, c: Char) = p.allNeighbours().count { this[it] == c }

fun List<String>.parse(): Forest = mutableMapOf<Point, Char>().apply {
    forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            this[Point(x, y)] = c
        }
    }
}
