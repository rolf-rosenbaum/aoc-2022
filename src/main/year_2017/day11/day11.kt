package year_2017.day11

import kotlin.math.absoluteValue
import readInput

fun part1(input: List<String>): Int {

    val directions = input.first().split(",")

    val origin = HexPoint(0, 0, 0)
    val endPoint = directions.fold(origin) { point, dir ->
        point.step(dir)
    }
    return origin.distance(endPoint)
}

fun part2(input: List<String>): Int {
    val directions = input.first().split(",")
    val origin = HexPoint(0, 0, 0)
    val steps = mutableListOf(origin)

    directions.forEach { dir ->
        steps.add(steps.last().step(dir))
    }

    return steps.maxOf { it.distance(origin) }

}

fun main() {
    val input = readInput("main/year_2017/day11/Day11")
    println(part1(input))
    println(part2(input))
}


data class HexPoint(val x: Int, val y: Int, val z: Int) {

    fun step(direction: String): HexPoint =
        when (direction) {
            "n" -> HexPoint(x, y + 1, z - 1)
            "s" -> HexPoint(x, y - 1, z + 1)
            "ne" -> HexPoint(x + 1, y, z - 1)
            "nw" -> HexPoint(x - 1, y + 1, z)
            "se" -> HexPoint(x + 1, y - 1, z)
            "sw" -> HexPoint(x - 1, y, z + 1)
            else -> error("illegal direction")
        }

    fun distance(other: HexPoint): Int =
        maxOf(
            (this.x - other.x).absoluteValue,
            (this.y - other.y).absoluteValue,
            (this.z - other.z).absoluteValue
        )
}