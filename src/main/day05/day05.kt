package day05

import java.util.*
import readInput

val stackIndices = listOf(1, 5, 9, 13, 17, 21, 25, 29, 33)
var stacks: List<Stack<Char>> = emptyList()
var moves: List<Move> = emptyList()

val regex by lazy { """move (\d+) from (\d) to (\d)""".toRegex() }

fun main() {
    val input = readInput("main/day05/Day05")

    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): String {

    input.parse()
    rearrange(::movingSingleCrate)

    return message()
}

fun part2(input: List<String>): String {

    input.parse()
    rearrange(::movingPartialStack)

    return message()
}

private fun List<String>.parse() {
    stacks = take(8).parseToStacks()
    moves = drop(10).parseMoves()
}

private fun rearrange(move: (Move, List<Stack<Char>>) -> Unit) {
    moves.forEach {
        move(it, stacks)
    }
}

private fun movingSingleCrate(move: Move, stacks: List<Stack<Char>>) {
    repeat(move.numberOfCrates) { _ ->
        stacks[move.to - 1].push(stacks[move.from - 1].pop())
    }
}

private fun movingPartialStack(move: Move, stacks: List<Stack<Char>>) {
    val tmp = mutableListOf<Char>()
    repeat(move.numberOfCrates) { _ ->
        tmp.add(stacks[move.from - 1].pop())
    }
    tmp.reversed().forEach { c ->
        stacks[move.to - 1].push(c)
    }
}

private fun message() = stacks.map {
    it.pop()
}.joinToString("")

fun List<String>.parseMoves(): List<Move> {
    return this.map {
        regex.matchEntire(it)?.destructured?.let { (count, from, to) ->
            Move(from.toInt(), to.toInt(), count.toInt())
        } ?: error("")
    }
}

fun List<String>.parseToStacks(): List<Stack<Char>> {
    val result = mutableListOf<Stack<Char>>()
    repeat(9) { result.add(Stack()) }
    this.reversed().forEach { s ->
        s.forEachIndexed { index, c ->
            if (index in stackIndices) {
                if (c != ' ') result[(index / 4)].push(c)
            }
        }
    }
    return result
}

data class Move(val from: Int, val to: Int, val numberOfCrates: Int)