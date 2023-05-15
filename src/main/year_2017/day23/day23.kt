package year_2017.day23

import readInput
import second

val registers = mutableMapOf(
    'a' to 0,
    'b' to 0,
    'c' to 0,
    'd' to 0,
    'e' to 0,
    'f' to 0,
    'g' to 0,
    'h' to 0,
)

fun part1(input: List<String>): Int {
    val instructions = input.map(String::toInstruction)
    return runInstructions(instructions)
}

private fun runInstructions(instructions: List<Instruction>): Int {
    var counter = 0
    var index = 0
    while (index in instructions.indices) {
        print("index $index, counter $counter \r")
        val instruction = instructions[index]
        when (instruction.command) {
            "set" -> {
                registers[instruction.x] = if (instruction.yIsNumber()) instruction.y.toInt() else registers[instruction.y.first()]!!
                index++
            }

            "sub" -> {
                registers[instruction.x] =
                    registers[instruction.x]!! - if (instruction.yIsNumber()) instruction.y.toInt() else registers[instruction.y.first()]!!
                index++
            }

            "mul" -> {
                registers[instruction.x] =
                    registers[instruction.x]!! * if (instruction.yIsNumber()) instruction.y.toInt() else registers[instruction.y.first()]!!
                counter++
                index++
            }

            "jnz" -> {
                val v = if (instruction.x.isDigit()) instruction.x.digitToInt() else registers[instruction.x]!!
                if (v != 0) index += instruction.y.toInt()
                else index++
            }

            else -> error("unknown instruction ${instruction.command}")
        }
    }
    return counter
}

fun part2(input: List<String>): Int {
    val start = input.first().split(" ")[2].toInt() * 100 + 100000
    return (start..start + 17000 step 17).count {
        !it.toBigInteger().isProbablePrime(5)
    }
}

fun main() {
    val input = readInput("main/year_2017/day23/Day23")
    println(part1(input))
    println(part2(input))
}

fun String.toInstruction(): Instruction {
    return this.split(" ").let {
        Instruction(command = it.first(), x = it.second().first(), y = it[2])
    }
}

data class Instruction(val command: String, val x: Char, val y: String) {
    fun yIsNumber() = y.first().isDigit() || y.first() == '-'
}
