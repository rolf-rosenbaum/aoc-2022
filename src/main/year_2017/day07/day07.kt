package year_2017.day07

import kotlin.math.absoluteValue
import readInput
import second

fun part1(input: List<String>): String {
    val programs = input.map(String::parse)

    return programs.findBottomProgram()
}

fun part2(input: List<String>): Int {
    val programs = input.map(String::parse)
    allPrograms = programs.associateBy { it.name }.toMutableMap()

    return allPrograms[programs.findBottomProgram()]!!.findImbalance()
}

var allPrograms: Map<String, Program> = mapOf()

fun main() {
    val input = readInput("main/year_2017/day07/Day07")
    println(part1(input))
    println(part2(input))
}

fun Collection<Program>.findBottomProgram(): String {
    val allNames = this.flatMap { p -> listOf(p.name.trim()) + p.programsCarried.map { it.trim() } }
    val nameCounts = allNames.associateBy { s -> allNames.count { it == s } }

    return nameCounts[1]!!
}

data class Program(
    val name: String,
    val weight: Int,
    val programsCarried: List<String>
) {
    private fun totalWeightCarried(): Int {
        return weight + programsCarried
            .sumOf { name ->
                allPrograms[name.trim()]!!.totalWeightCarried()
            }
    }

    private fun childrenAreBalanced() =
        programsCarried.map { allPrograms[it.trim()]!!.totalWeightCarried() }.distinct().size == 1

    fun findImbalance(imbalance: Int? = null): Int =
        if (imbalance != null && childrenAreBalanced()) {
            weight - imbalance
        } else {
            val subTrees = programsCarried.groupBy { allPrograms[it.trim()]!!.totalWeightCarried() }
            val outOfBalanceTree = subTrees.minBy { it.value.size }.value.first().trim()

            allPrograms[outOfBalanceTree]!!.findImbalance(imbalance = imbalance ?: subTrees.keys.reduce { a, b -> a - b }.absoluteValue)
        }
}

fun String.parse(): Program {
    return this.split("->").let {
        val split = it.first().split((" ("))
        val name = split.first().trim()
        val weight = split.second().trim().dropLast(1).toInt()
        val programs = if (it.size > 1) {
            it.second().split((", ").trim())
        } else emptyList()
        Program(name.trim(), weight, programs.map { p -> p.trim() })
    }
}