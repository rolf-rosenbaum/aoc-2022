package day01

import readInput

fun part1(input: List<String>): Int {
    return sumsByElf(input).maxOf { it }
}

private fun sumsByElf(input: List<String>): MutableList<Int> {
    val sums = mutableListOf<Int>()
    var sum = 0
    input.forEach {
        if (it.isBlank()) {
            sums.add(sum)
            sum = 0
        } else sum += it.toInt()
    }
    return sums
}

fun part2(input: List<String>): Int {
    return sumsByElf(input).sorted().takeLast(3).sum()
}

fun main() {
    val input = readInput("main/day01/Day01")
    println(part1(input))
    println(part2(input))
}
