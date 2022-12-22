package day21

import readInput

fun main() {
    val input = readInput("main/day21/Day21")

    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Long {
    val monkeys = input.map { it.toMonkey() }.associateBy { it.name }
    return solvePart1(monkeys.toMutableMap(), monkeys["root"]!!)
}

fun part2(input: List<String>): Long {
    var humanNumber = 1000000000000L
    val monkeys = input.map { it.toMonkey() }.associateBy { it.name }
    while (true) {
        if (humanNumber % 10000L == 0L) println(humanNumber)
        if (checkForMatch(monkeys.toMutableMap(), humanNumber)) return humanNumber
        humanNumber++
    }

    return -1
}

fun checkForMatch(monkeys: MutableMap<String, Monkey>, humanNumber: Long): Boolean {
    val rootMonkey = monkeys["root"]!!

    val human = monkeys["humn"]!!.copy(number = humanNumber)
    monkeys["humn"] = human

    val firstMonkey = monkeys[rootMonkey.op?.first!!]!!
    val secondMonkey = monkeys[rootMonkey.op.second]!!
    val first = solvePart1(monkeys, firstMonkey)
    val second = solvePart1(monkeys, secondMonkey)

    return (first != null && first == second)
}

fun solvePart1(monkeys: MutableMap<String, Monkey>, current: Monkey): Long =
    if (current.number != null) {
        current.number
    } else {
        val firstMonkey = monkeys[current.op?.first] ?: error("out of monkeys")
        val secondMonkey = monkeys[current.op?.second] ?: error("out of monkeys")
        val firstNumber = solvePart1(monkeys, firstMonkey)
        monkeys[firstMonkey.name] = firstMonkey.copy(number = firstNumber)

        val secondNumber = solvePart1(monkeys, secondMonkey)
        monkeys[secondMonkey.name] = secondMonkey.copy(number = secondNumber)

        val func = current.op?.operator?.func!!
        func.invoke(firstNumber, secondNumber)
    }


fun String.toMonkey(): Monkey {
    return split(": ").let { (name, rest) ->
        if (rest.all(Char::isDigit)) {
            Monkey(name = name, number = rest.toLong())
        } else {
            rest.split(" ").let { (first, op, second) ->
                Monkey(name = name, op = MonkeyOp(first = first, second = second, operator = Operator.fromString(op)))
            }
        }
    }
}

data class Monkey(val name: String, val number: Long? = null, val op: MonkeyOp? = null)

data class MonkeyOp(val first: String, val second: String, val operator: Operator)

enum class Operator(val func: (Long, Long) -> Long) {
    PLUS({ a, b -> a + b }),
    MINUS({ a, b -> a - b }),
    TIMES({ a, b -> a * b }),
    DIV({ a, b -> a / b });

    fun opposite() = when (this) {
        PLUS -> MINUS
        MINUS -> PLUS
        TIMES -> DIV
        DIV -> TIMES
    }

    companion object {
        fun fromString(operation: String) =
            when (operation.trim()) {
                "+" -> PLUS
                "-" -> MINUS
                "*" -> TIMES
                "/" -> DIV
                else -> error("illegal operation")
            }
    }

}
