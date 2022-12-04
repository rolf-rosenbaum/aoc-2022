package day04

import readInput

fun part1(input: List<String>): Int = input.solve(IntRange::fullyContains)

fun part2(input: List<String>): Int = input.solve(IntRange::overlapsWith)

val regex by lazy { """(\d+)-(\d+),(\d+)-(\d+)""".toRegex() }

fun main() {
    val input = readInput("main/day04/Day04")
    println(part1(input))
    println(part2(input))
}

private fun List<String>.solve(check: IntRange.(IntRange) -> Boolean) =
    map { it.toRangesPairs() }.count { it.first.check(it.second) }

fun String.toRangesPairs(): Pair<IntRange, IntRange> {
    return regex.matchEntire(this)?.destructured?.let { (a, b, c, d) ->
        a.toInt()..b.toInt() to c.toInt()..d.toInt()
    } ?: error("invalid line")
}

fun IntRange.fullyContains(other: IntRange) =
    contains(other.first) && contains(other.last)

fun IntRange.overlapsWith(other: IntRange) =
    contains(other.first) || contains(other.last)
