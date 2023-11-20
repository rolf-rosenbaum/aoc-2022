package year_2016.day15

import readInput

val reg = """\b\d+\b""".toRegex()

fun main() {

    val input = readInput("main/year_2016/day15/Day15")

    println(part1(input))
    println(part2(input))
}


fun part1(input: List<String>): Int {
    return part2(input.dropLast(1))
}

fun part2(input: List<String>): Int {
    val discs = input.map(String::toDisc)
    var time = 0
    while (true) {
        if (discs.all {
                (it.layer + it.startingPosition + time) % it.positions == 0
            })
            return time
        time++
    }
}

data class Disc(
    val layer: Int,
    val positions: Int,
    val startingPosition: Int
)


fun String.toDisc(): Disc {
    val (layer, positions, _, startingPosition) = reg.findAll(this).toList()
    return Disc(layer.value.toInt(), positions.value.toInt(), startingPosition.value.toInt())

}

