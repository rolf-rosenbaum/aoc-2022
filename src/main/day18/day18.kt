package day18

import readInput

fun main() {
    val input = readInput("main/day18/Day18")
    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Int = input.toCubes().surfaceArea()

fun part2(input: List<String>): Int = exteriorSurfaceArea(input.toCubes())

private fun List<String>.toCubes() = map { line ->
    val (x, y, z) = line.split(",")
    Cube(x.toInt(), y.toInt(), z.toInt())
}

private fun List<Cube>.surfaceArea(): Int = sumOf { it.neighbours().filter { c -> c !in this }.size }

private fun exteriorSurfaceArea(cubes: List<Cube>): Int {
    val minX = cubes.minOf { it.x } - 1
    val maxX = cubes.maxOf { it.x } + 1
    val minY = cubes.minOf { it.y } - 1
    val maxY = cubes.maxOf { it.y } + 1
    val minZ = cubes.minOf { it.z } - 1
    val maxZ = cubes.maxOf { it.z } + 1

    val surface = mutableSetOf<Cube>()
    val queue = mutableListOf(Cube(minX, minY, minZ))
    while (queue.isNotEmpty()) {
        val current = queue.removeLast()
        if (current in cubes) continue
        val (x, y, z) = current
        if (x !in minX..maxX || y !in minY..maxY || z !in minZ..maxZ) continue
        if (surface.add(current)) queue.addAll(current.neighbours())
    }
    return cubes.sumOf { it.neighbours().filter { c -> c in surface }.size }
}

data class Cube(val x: Int, val y: Int, val z: Int) {
    fun neighbours() = listOf(
        Cube(x + 1, y, z), Cube(x - 1, y, z),
        Cube(x, y + 1, z), Cube(x, y - 1, z),
        Cube(x, y, z + 1), Cube(x, y, z - 1)
    )
}
