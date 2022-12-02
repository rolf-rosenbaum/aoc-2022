package day02

import readInput
import reverse
import second

const val ROCK = "A"
const val PAPER = "B"
const val SCISSORS = "C"

const val ROCK_ = "X"
const val PAPER_ = "Y"
const val SCISSORS_ = "Z"

const val LOSE = "X"
const val DRAW = "Y"
const val WIN = "Z"

fun part1(input: List<String>): Int {

    return input.toPairs().sumOf {
        when (it) {
            ROCK to ROCK_ -> 1 + 3
            ROCK to PAPER_ -> 2 + 6
            ROCK to SCISSORS_ -> 3 + 0
            PAPER to ROCK_ -> 1 + 0
            PAPER to PAPER_ -> 2 + 3
            PAPER to SCISSORS_ -> 3 + 6
            SCISSORS to ROCK_ -> 1 + 6
            SCISSORS to PAPER_ -> 2 + 0
            SCISSORS to SCISSORS_ -> 3 + 3

            else -> error("invalid pair")
        }.toInt()
    }
}

fun part2(input: List<String>): Int {
    return input.toPairs().sumOf {
        when (it.reverse()) {
            LOSE to ROCK -> 0 + 3
            DRAW to ROCK -> 3 + 1
            WIN to ROCK -> 6 + 2
            LOSE to PAPER -> 0 + 1
            DRAW to PAPER -> 3 + 2
            WIN to PAPER -> 6 + 3
            LOSE to SCISSORS -> 0 + 2
            DRAW to SCISSORS -> 3 + 3
            WIN to SCISSORS -> 6 + 1

            else -> error("invalid pair")
        }.toInt()
    }
}

fun main() {
    val input = readInput("main/day02/Day02")
    println(part1(input))
    println(part2(input))
}

fun List<String>.toPairs() = map {
    it.split(" ").let { pair ->
        pair.first() to pair.second()
    }
}