package day15

import Point
import day15.CavePoint.Sensor
import kotlin.math.abs
import readInput
import union

val regex = """.+x=(-?\d+).*y=(-?\d+):.*x=(-?\d+).*y=(-?\d+)""".toRegex()

const val prd_row = 2_000_000
const val test_row = 10

val prd_range = 0..4000000
val test_range = 0..20

typealias Cave = MutableSet<CavePoint>

sealed class CavePoint(open val location: Point) {

    data class BeaconExcluded(override val location: Point) : CavePoint(location)

    data class Sensor(override val location: Point, val beacon: Point) : CavePoint(beacon) {
        val beaconDistance
            get() = location.distanceTo(beacon)
    }

    override fun equals(other: Any?): Boolean {
        return if (other is CavePoint) location == other.location else false
    }

    override fun hashCode(): Int {
        return location.hashCode()
    }
}

fun main() {
    val input = readInput("main/day15/Day15_test")
    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Int {
    val cave = input.parse()

    val row = if (input.size < 20) test_row else prd_row
    cave.markNoBeaconArea(row)
    val beaconLocations = cave.filterIsInstance<Sensor>().map { it.beacon }.toSet()
    return cave.count { it.location.y == row && it.location !in beaconLocations }
}

fun part2(input: List<String>): Long {
    val cave = input.parse()
    val range = if (input.size > 20) prd_range else test_range
    range.forEach { row ->
        cave.lineRangesFor(row).reduce { acc, range -> (acc.union(range)) ?: return (acc.last + 1) * 4_000_000L + row }
    }
    return -1
}

fun Cave.markNoBeaconArea(row: Int) {
    val sensors = filterIsInstance<Sensor>()
    sensors.forEach { sensor ->
        (sensor.location.x - sensor.beaconDistance..sensor.location.x + sensor.beaconDistance).forEach { x ->
            val candidate = Point(x, row)
            if (candidate.distanceTo(sensor.location) <= sensor.beaconDistance) add(CavePoint.BeaconExcluded(candidate))
        }
    }
}

fun Cave.lineRangesFor(row: Int): List<IntRange> {
    return filterIsInstance<Sensor>().map { sensor ->
        val distance = sensor.beaconDistance - abs(row - sensor.location.y)
        sensor.location.x - distance..sensor.location.x + distance
    }.sortedBy { it.first }
}

fun List<String>.parse(): Cave = map {
    regex.matchEntire(it)?.destructured?.let { (sX, sY, bX, bY) ->
        Sensor(location = Point(x = sX.toInt(), y = sY.toInt()), beacon = Point(bX.toInt(), bY.toInt()))
    } ?: error("PARSER PROBLEM")
}.toMutableSet()
