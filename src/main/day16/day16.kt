package day16

import kotlin.math.max
import readInput

const val START = "AA"

val flowRegex = """(\d+)""".toRegex()
val valveRegex = """[A-Z]{2}""".toRegex()

var totalTime = 30
var maxPressureRelease = 0
var allValves: Map<String, Valve> = mapOf()
var shortestPaths: MutableMap<String, MutableMap<String, Int>> = mutableMapOf()

fun main() {
    val input = readInput("main/day16/Day16_test")
    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Int {
    prepareSearch(input)

    checkAllPaths(0, START, emptySet(), 0)
    return maxPressureRelease
}

fun part2(input: List<String>): Int {
    totalTime = 26
    prepareSearch(input)

    checkAllPaths(0, START, emptySet(), 0, true)
    return maxPressureRelease
}

private fun prepareSearch(input: List<String>) {
    maxPressureRelease = 0
    val valves = input.map { it.parse() }

    allValves = valves.associateBy { it.id }
    shortestPaths =
        shortestPathsFromEachTunnelToAllOtherTunnels(
            valves.associate {
                it.id to it.neighbouringValves.associateWith { 1 }
                    .toMutableMap()
            }.toMutableMap()
        )
}

private fun checkAllPaths(currentPressureRelease: Int, currentValveId: String, visited: Set<String>, time: Int, withElefant: Boolean = false) {
    maxPressureRelease = max(maxPressureRelease, currentPressureRelease)
    shortestPaths[currentValveId]!!.forEach { (valveId, distance) ->
        if (!visited.contains(valveId) && time + distance + 1 < totalTime) {
            checkAllPaths(
                currentPressureRelease = currentPressureRelease + (totalTime - time - distance - 1) * allValves[valveId]?.flow!!,
                currentValveId = valveId,
                visited = visited + valveId,
                time = time + distance + 1,
                withElefant = withElefant
            )
        }
    }
    if (withElefant) {
        checkAllPaths(currentPressureRelease, START, visited, 0, false)
    }
}

private fun shortestPathsFromEachTunnelToAllOtherTunnels(shortestPaths: MutableMap<String, MutableMap<String, Int>>): MutableMap<String, MutableMap<String, Int>> {
    shortestPaths.keys.forEach { a ->
        shortestPaths.keys.forEach { b ->
            shortestPaths.keys.forEach { c ->
                val ab = shortestPaths[b]?.get(a) ?: 100
                val ac = shortestPaths[a]?.get(c) ?: 100
                val bc = shortestPaths[b]?.get(c) ?: 100
                if (ab + ac < bc)
                    shortestPaths[b]?.set(c, ab + ac)
            }
        }
    }
    shortestPaths.values.forEach {
        it.keys.mapNotNull { key -> if (allValves[key]?.flow == 0) key else null }
            .forEach { toRemove ->
                it.remove(toRemove)
            }
    }
    return shortestPaths
}

fun String.parse(): Valve {
    val valves = valveRegex.findAll(this).map { it.groupValues.first() }.toList()
    val flow = flowRegex.findAll(this).first().groupValues.first().toInt()
    val tunnels = valves.drop(1)
    return Valve(id = valves.first(), flow = flow, neighbouringValves = tunnels)
}

data class Valve(val id: String, val flow: Int, val neighbouringValves: List<String>)
