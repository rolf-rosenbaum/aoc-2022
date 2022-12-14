package day20

import readInput

const val encryptionKey = 811589153L

fun main() {
    val input = readInput("main/day20/Day20_test")
    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Long {
    val original = input.mapIndexed { index, s -> Num(originalPosition = index, number = s.toLong()) }.toMutableList()
    val mixed = mix(original)

    return summedCoordinates(mixed)
}

fun part2(input: List<String>): Long {
    val original = input.mapIndexed { index, s ->
        Num(originalPosition = index, number = s.toLong() * encryptionKey)
    }.toMutableList()

    val mixed = generateSequence(original) { mix(original, it).toMutableList() }.drop(10).first()
    return summedCoordinates(mixed)
}

private fun mix(list: MutableList<Num>, original: MutableList<Num> = list, position: Int = 0): List<Num> {
    if (position == list.size) return list

    val index = list.indexOfFirst {
        it == original.firstOrNull { o ->
            o.originalPosition == position
        }
    }
    val current = list[index]
    var newIndex = (index + current.number) % (list.size - 1)
    if (newIndex <= 0) newIndex += list.size - 1
    list.removeAt(index)
    list.add(newIndex.toInt(), current.move())

    return mix(list = list, original = list, position = position + 1)
}

private fun summedCoordinates(mixed: List<Num>): Long {
    val zero = mixed.indexOfFirst { it.number == 0L }
    val a = mixed[(zero + 1000) % mixed.size].number
    val b = mixed[(zero + 2000) % mixed.size].number
    val c = mixed[(zero + 3000) % mixed.size].number
    return a + b + c
}

data class Num(val originalPosition: Int, val currentPosition: Int = originalPosition, val number: Long, val moved: Int = 0) {
    fun move() = copy(moved = moved + 1)
}