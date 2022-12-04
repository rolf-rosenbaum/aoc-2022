package day04

import readInput
import second

fun part1(input: List<String>): Int = input.solve(IntRange::fullyContains)

fun part2(input: List<String>): Int = input.solve(IntRange::overlapsWith)

fun main() {
    val input = readInput("main/day04/Day04")
    println(part1(input))
    println(part2(input))
}

private fun List<String>.solve(testFunction: (IntRange, IntRange) -> Boolean) =
    map {
        it.toRanges()
    }.count {
        testFunction(it.first, it.second)
    }

fun String.toRanges(): Pair<IntRange, IntRange> {
    return split(",").let {
        it.first().split("-").let { range -> range.first().toInt()..range.second().toInt() } to
                it.second().split("-")
                    .let { range -> range.first().toInt()..range.second().toInt() }
    }
}

fun IntRange.fullyContains(other: IntRange) =
    contains(other.first) && contains(other.last)

fun IntRange.overlapsWith(other: IntRange) =
    contains(other.first) || contains(other.last)
