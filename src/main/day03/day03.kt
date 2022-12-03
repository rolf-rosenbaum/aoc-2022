package day03

import readInput

fun part1(input: List<String>): Int =
    input.sumOf {
        it.splitHalf().commonCharacter().priority()
    }

fun part2(input: List<String>): Int {
    return input.windowed(3, 3).sumOf {
        it.commonCharacter().priority()
    }
}

fun main() {
    val input = readInput("main/day03/Day03")
    println(part1(input))
    println(part2(input))
}

fun String.splitHalf() = listOf(substring(0, length / 2), substring(length / 2, length))

fun Char.priority() = if (isLowerCase()) this - 'a' + 1 else this - 'A' + 27

fun List<String>.commonCharacter(): Char = first().first { c ->
    this.all { it.contains(c) }
}