package day23

import Point
import readInput

private typealias Grove = Set<Point>

val directionsToCheck = mutableListOf<Point.() -> List<Point>>(
    Point::northernNeighbours,
    Point::southernNeighbours,
    Point::westernNeighbours,
    Point::easternNeighbours
)

fun main() {
    val input = readInput("main/day23/Day23")

    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Int {

    val grove = input.toGrove()
    grove.print()
    val groveAfterTenSteps = generateSequence(grove) { step(it).second }.drop(10).first()
    groveAfterTenSteps.print()

    return (1 + groveAfterTenSteps.maxOf { it.x } - groveAfterTenSteps.minOf { it.x }) * (1 + groveAfterTenSteps.maxOf { it.y } - groveAfterTenSteps.minOf { it.y }) - groveAfterTenSteps.size
}

fun part2(input: List<String>): Int {
    var grove = input.toGrove()
    grove.print()
    var result: Pair<Grove, Grove> = emptySet<Point>() to grove
    var counter = 0
    do {
        result = step(result.second, counter)
        counter++

    } while (result.first != result.second)
    return counter
//    return generateSequence(result) { step(it.second) }.takeWhile { it.first != it.second }.count() +1

}


private fun step(grove: Set<Point>, counter: Int = 0): Pair<Grove, Grove> {

    val proposedSteps = grove.proposedSteps()

    directionsToCheck.add(directionsToCheck.removeFirst())

    val stepsByElf = proposedSteps.associateBy { it.first }
    val possiblePositions = proposedSteps.associateTo(mutableMapOf()) { (_, next) ->
        next to proposedSteps.count { it.second == next }
    }.filter {
        it.value == 1
    }.keys
    if (possiblePositions.isEmpty()) println(counter)

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

fun Grove.print() {
    (minOf { it.y }..maxOf { it.y }).forEach { y ->
        (minOf { it.x }..maxOf { it.x }).forEach { x ->
            print(if (contains(Point(x, y))) "#" else ".")
        }
        println()
    }
    println()
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
