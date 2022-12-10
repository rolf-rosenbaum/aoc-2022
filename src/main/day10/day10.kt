package day10

import readInput

fun main() {
    val input = readInput("main/day10/Day10")

    println(part1(input))
    part2(input)
}

fun part1(input: List<String>): Int {
    val cycles = input.toCycles()
    val interestingCycles = listOf(20, 60, 100, 140, 180, 220)

    return interestingCycles.sumOf { cycles[it - 1] * it }
}

fun part2(input: List<String>) {
    val cycleMap = input.toCycles()

    val crt = cycleMap.mapIndexed { index, register ->
        val sprite = register - 1..register + 1
        if (index % 40 in sprite) "#" else " "
    }

    crt.forEachIndexed { index, s ->
        print(s)
        if (index % 40 == 39) println()
    }
}

private fun List<String>.toCycles(): List<Int> {
    var x = 1
    val cycles = mutableListOf<Int>()
    forEach {
        if (it.startsWith("noop")) {
            cycles.add(x)
        } else {
            repeat(2) { cycles.add(x) }
            x += it.split(" ").last().toInt()
        }
    }
    return cycles
}
