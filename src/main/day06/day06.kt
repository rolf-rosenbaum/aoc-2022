package day06

import readInput

fun main() {
    val input = readInput("main/day06/Day06")

    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Int = solve(input, 4)

fun part2(input: List<String>): Int = solve(input, 14)

private fun solve(input: List<String>, size: Int): Int =
    input.first().windowed(size).first {
        it.toSet().size == size
    }.let {
        input.first().indexOf(it) + size
    }
