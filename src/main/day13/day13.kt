package day13

import java.util.*
import readInput
import second

sealed interface Packet : Comparable<Packet> {
    data class ListPacket(val packets: MutableList<Packet> = mutableListOf()) : Packet {

        override fun compareTo(other: Packet): Int {
            return when (other) {
                is IntPacket -> this.compareTo(other.toListPacket())
                is ListPacket -> packets.compareTo(other.packets)
            }
        }

        private fun Iterable<Packet>.compareTo(other: Iterable<Packet>): Int {
            val left = Stack<Packet>()
            this.reversed().forEach { left.push(it) }
            val right = Stack<Packet>()
            other.reversed().forEach { right.push(it) }

            while (left.isNotEmpty()) {
                val l = left.pop()
                val r = if (right.isNotEmpty()) right.pop() else return 1
                val comp = l.compareTo(r)
                if (comp != 0) return comp
            }
            // left is empty is right also empty?
            return if (right.isEmpty()) 0 else -1
        }
    }

    data class IntPacket(val value: Int) : Packet {
        override fun compareTo(other: Packet): Int {
            return when (other) {
                is ListPacket -> toListPacket().compareTo(other)
                is IntPacket -> value.compareTo(other.value)
            }
        }
    }

    fun toListPacket(): Packet = ListPacket(mutableListOf(this))

    companion object {
        fun parse(input: String): Packet =
            toPacket(
                input.split("""((?<=[\[\],])|(?=[\[\],]))""".toRegex())
                    .filter { it.isNotBlank() }
                    .filterNot { it == "," }
                    .iterator()
            )

        private fun toPacket(input: Iterator<String>): Packet {
            val packets = mutableListOf<Packet>()
            while (input.hasNext()) {
                when (val symbol = input.next()) {
                    "]" -> return ListPacket(packets)
                    "[" -> packets.add(toPacket(input))
                    else -> packets.add(IntPacket(symbol.toInt()))
                }
            }
            return ListPacket(packets)
        }
    }
}

fun main() {
    val input = readInput("main/day13/Day13")
    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Int {
    val list = input.filter(String::isNotBlank).map {
        Packet.parse(it)
    }.chunked(2)

    return list.mapIndexed { index, signals ->
        if (signals.first() < signals.second())
            index + 1
        else
            0
    }.sum()
}

fun part2(input: List<String>): Int {

    val firstDivider = Packet.parse("[[2]]")
    val secondDivider = Packet.parse("[[6]]")

    val list = (input.filter(String::isNotBlank).map {
        Packet.parse(it)
    } + listOf(firstDivider, secondDivider)).sorted()

    return (list.indexOf(firstDivider) + 1) * (list.indexOf(secondDivider) + 1)
}
