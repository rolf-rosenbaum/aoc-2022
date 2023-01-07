package day25

import kotlin.math.pow
import kotlin.math.roundToLong
import readInput

val UNSNAFU = mapOf(
    "=" to -2.0,
    "-" to -1.0,
    "0" to 0.0,
    "1" to 1.0,
    "2" to 2.0,
)

fun main() {
    val input = readInput("main/day25/Day25")

    println(part1(input))
}

fun part1(input: List<String>): String {

    val numbers = input.associateBy { it.unsnafucate() }

    return numbers.keys.sum().snafucate()
}

fun Long.snafucate(): String {
    return generateSequence(this) { (it + 2) / 5 }
        .takeWhile { it != 0L }.toList()
        .map { "012=-"[(it % 5).toInt()] }
        .joinToString("").reversed()
}

fun String.unsnafucate(): Long {
    return indices.sumOf {
        (5.0.pow(it) * UNSNAFU[this[length - it - 1].toString()]!!.toLong()).roundToLong()
    }
} 
