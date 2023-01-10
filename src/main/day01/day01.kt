package day01

import readInput

fun part1(input: List<String>): Int {
    return sumsByElf(input).maxOf { it }
}

private fun sumsByElf(input: List<String>): List<Int> {
    return input.joinToString("\n").split("\n\n")
        .map {
            it.split("\n")
                .sumOf { s -> s.toInt() }
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
