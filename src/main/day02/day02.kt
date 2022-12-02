package day02

import readInput
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
        when (it) {
            ROCK to LOSE -> 0 + 3
            ROCK to DRAW -> 3 + 1
            ROCK to WIN -> 6 + 2
            PAPER to LOSE -> 0 + 1
            PAPER to DRAW -> 3 + 2
            PAPER to WIN -> 6 + 3
            SCISSORS to LOSE -> 0 + 2
            SCISSORS to DRAW -> 3 + 3
            SCISSORS to WIN -> 6 + 1

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