package year_2017.day01

import readInput

fun part1(input: List<String>): Int = input.first().findSum()

fun part2(input: List<String>): Int = input.first().let { it.findSum { n -> n + it.length / 2 } }

fun main() {
    val input = readInput("main/year_2017/day01/Day01")
    println(part1(input))
    println(part2(input))
}

private fun String.findSum(findIndexToCompare: (Int) -> Int = { n -> n + 1 }) = mapIndexed { index, c ->
    if (c == this[(findIndexToCompare(index)) % length])
        c.digitToInt()
    else 0
}.sum()
