package day05

import java.util.*
import readInput


val stackIndices = listOf(1, 5, 9, 13, 17, 21, 25, 29, 33)

val regex by lazy { """move (\d+) from (\d) to (\d)""".toRegex() }

fun part1(input: List<String>): String {
    val stacks = input.take(8).parseToStacks()
    val moves = input.drop(10).parseMoves()

    moves.forEach {
        repeat(it.numberOfCrates) { _ ->
            stacks[it.to - 1].push(stacks[it.from - 1].pop())
        }
    }
    return stacks.map {
        it.pop()
    }.joinToString("")
}

fun part2(input: List<String>): String {

    val stacks = input.take(8).parseToStacks()
    val moves = input.drop(10).parseMoves()
    moves.forEach {
        val tmp = mutableListOf<Char>()
        repeat(it.numberOfCrates) { _ ->
            tmp.add(stacks[it.from - 1].pop())
        }
        tmp.reversed().forEach { c ->
            stacks[it.to - 1].push(c)
        }
    }
    return stacks.map {
        it.pop()
    }.joinToString("")




    return "error"
}

fun main() {
    val input = readInput("main/day05/Day05")
    println(part1(input))
    println(part2(input))
}


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