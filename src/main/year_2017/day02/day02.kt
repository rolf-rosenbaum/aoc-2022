package year_2017.day02

import readInput

fun part1(input: List<String>): Int = input.map { line ->
    line.split(" ").map { it.toInt() }.sortedDescending()
}.sumOf { it.first() - it.last() }

fun part2(input: List<String>): Int = input.map { line ->
    line.split(" ").map { it.toInt() }.sortedDescending()
}.sumOf { line ->
    line.findDivisionResult()
}


fun main() {
    val input = readInput("main/year_2017/day02/Day02")
    println(part1(input))
    println(part2(input))
}

fun List<Int>.findDivisionResult(): Int {
    forEachIndexed { index, candidate ->
        (index + 1 until size).forEach {
            if (candidate % this[it] == 0) return candidate / this[it]
        }
    }
    error("no even division found")
}
