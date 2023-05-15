package day23

import Point
import readInput

private typealias Grove = Set<Point>

var directionsToCheck = mutableListOf(
    Point::northernNeighbours,
    Point::southernNeighbours,
    Point::westernNeighbours,
    Point::easternNeighbours
)

fun main() {
    val input = readInput("main/day23/Day23")

    println(year_2017.day23.part1(input))
    println(year_2017.day23.part2(input))
}

fun part1(input: List<String>): Int {
    return generateSequence(input.toGrove()) { step(it).second }.drop(10).first().let { tenth ->
        (1 + tenth.maxOf { it.x } - tenth.minOf { it.x }) *
                (1 + tenth.maxOf { it.y } - tenth.minOf { it.y }) - tenth.size
    }
}

fun part2(input: List<String>): Int {
    directionsToCheck = mutableListOf(
        Point::northernNeighbours,
        Point::southernNeighbours,
        Point::westernNeighbours,
        Point::easternNeighbours
    )
    return generateSequence(emptySet<Point>() to input.toGrove()) { result ->
        step(result.second)
    }.takeWhile {
        it.first != it.second
    }.count()
}

private fun step(grove: Set<Point>): Pair<Grove, Grove> {

    val proposedSteps = grove.proposedSteps()
    directionsToCheck.add(directionsToCheck.removeFirst())

    val stepsByElf = proposedSteps.associateBy { it.first }
    val possiblePositions = proposedSteps.associateTo(mutableMapOf()) { (_, next) ->
        next to proposedSteps.count { it.second == next }
    }.filter {
        it.value == 1
    }.keys

    return grove to grove.map { elf ->
        if (possiblePositions.contains(stepsByElf[elf]?.second))
            stepsByElf[elf]!!.second
        else
            elf
    }.toSet()
}

fun Grove.proposedSteps(): List<Pair<Point, Point>> {
    val proposedSteps = mutableListOf<Pair<Point, Point>>()
    forEach { elf ->
        if (elf.allNeighbours().any { this.contains(it) }) {

            val direction = directionsToCheck.firstOrNull { findNeighbours ->
                findNeighbours.invoke(elf).dropLast(1).none { this.contains(it) }
            }
            if (direction != null) {
                proposedSteps.add(elf to (elf + direction.invoke(elf).last()))
            }
        }
    }
    return proposedSteps
}

fun List<String>.toGrove() =
    flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, s ->
            if (s == '#') Point(x, y) else null
        }
    }.toSet()

fun Point.northernNeighbours(): List<Point> {
    return listOf(
        Point(x - 1, y - 1),
        Point(x, y - 1),
        Point(x + 1, y - 1),
        Point(0, -1),
    )
}

fun Point.southernNeighbours(): List<Point> {
    return listOf(
        Point(x - 1, y + 1),
        Point(x, y + 1),
        Point(x + 1, y + 1),
        Point(0, 1),
    )
}

fun Point.easternNeighbours(): List<Point> {
    return listOf(
        Point(x + 1, y - 1),
        Point(x + 1, y),
        Point(x + 1, y + 1),
        Point(1, 0),
    )
}

fun Point.westernNeighbours(): List<Point> {
    return listOf(
        Point(x - 1, y - 1),
        Point(x - 1, y),
        Point(x - 1, y + 1),
        Point(-1, 0),
    )
}
