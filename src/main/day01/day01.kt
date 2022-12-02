package day01

import readInput

fun part1(input: List<String>): Int {
    return sumsByElf(input).maxOf { it }
}

private fun sumsByElf(input: List<String>): List<Int> {
    var sum = 0
    return input.map {
        if (it.isBlank()) {
            val tmp = sum
            sum = 0
            tmp
        } else {
            sum += it.toInt()
            0
        }
    }
}

fun part2(input: List<String>): Int {
    return sumsByElf(input).sorted().takeLast(3).sum()
}

fun main() {
    val input = readInput("main/day01/Day01")
    println(part1(input))
    println(part2(input))
}
