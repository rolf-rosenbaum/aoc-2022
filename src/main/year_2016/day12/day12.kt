package year_2016.day12


import readInput

fun main() {
    val input = readInput("main/year_2016/day12/Day12")

    println(part1(input))
    println(part2(input))
}

val registers = mutableMapOf(
    "a" to 0,
    "b" to 0,
    "c" to 0,
    "d" to 0,
)

fun part1(input: List<String>): Int {

    var index = 0

    while (index < input.size) {
        val line = input[index]
        when (line.substring(0, 3)) {
            "cpy" -> {
                val (_, x, reg) = line.split(" ")
                if (x in listOf("a", "b", "c", "d"))
                    registers[reg] = registers[x]!!
                else
                    registers[reg] = x.toInt()
                index++
            }

            "inc" -> {
                val (_, reg) = line.split(" ")
                registers[reg] = registers[reg]!! + 1
                index++
            }

            "dec" -> {
                val (_, reg) = line.split(" ")
                registers[reg] = registers[reg]!! - 1
                index++
            }

            "jnz" -> {
                val (_, x, y) = line.split(" ")
                if (x.all { it.isDigit() }) {
                    if (x.toInt() != 0) index += y.toInt()
                } else {
                    if (registers[x] != 0)
                        index += y.toInt()
                    else index++
                }
            }
        }

    }
    return registers["a"]!!
}

fun part2(input: List<String>): Int {
    registers["c"] = 1
    return part1(input)
}

