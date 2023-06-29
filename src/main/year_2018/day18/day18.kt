package year_2018.day18

import Point
import findPattern
import readInput

typealias Forest = Map<Point, Char>

private const val TREE = '|'
private const val OPEN = '.'
private const val LUMBER_YARD = '#'

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
    val (stepsBeforePattern, patternSize) = resourceValueList.findPattern()

    val indexInCycle = (stepCount - stepsBeforePattern) % patternSize
    return resourceValueList.drop(stepsBeforePattern)[indexInCycle]
}

fun Forest.step(): Forest = this.keys.associateWith { here ->
    when (this[here]) {
        OPEN -> if (this.countNeighborsOfKind(here, TREE) >= 3) TREE else OPEN
        TREE -> if (this.countNeighborsOfKind(here, LUMBER_YARD) >= 3) LUMBER_YARD else TREE
        LUMBER_YARD -> if (countNeighborsOfKind(here, LUMBER_YARD) > 0 && countNeighborsOfKind(here, TREE) > 0) LUMBER_YARD else OPEN
        else -> error("Something went wrong: $here, ${this[here]}")
    }
}

private fun Forest.resourceValue(): Int = count { it.value == LUMBER_YARD } * count { it.value == TREE }

fun Forest.resourceValueList(n: Int = 600): List<Int> = generateSequence(this) { it.step() }.take(n).map { it.resourceValue() }.toList()

fun Forest.countNeighborsOfKind(p: Point, c: Char) = p.allNeighbours().count { this[it] == c }

fun List<String>.parse(): Forest = mutableMapOf<Point, Char>().apply {
    forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            this[Point(x, y)] = c
        }
    }
}
