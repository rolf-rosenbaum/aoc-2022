package year_2017.day04

import readInput

fun part1(input: List<String>): Int = input.count {
    it.split(" ").size == it.split(" ").distinct().size
}

fun part2(input: List<String>): Int = input.count { line ->
    line.split(" ").map { it.toList().sorted() }.size == line.split(" ").map { it.toList().sorted() }.distinct().size
}

fun main() {
    val input = readInput("main/year_2017/day04/Day04")
    println(part1(input))
    println(part2(input))
}

