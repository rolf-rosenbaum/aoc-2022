package day11

private fun prdMonkeys() = mutableListOf(
    Monkey(
        id = 0,
        items = mutableListOf(72, 64, 51, 57, 93, 97, 68),
        operation = { n -> n * 19 },
        test = 17,
        throwToIfTrue = 4,
        throwToIfFalse = 7
    ),
    Monkey(
        id = 1,
        items = mutableListOf(62),
        operation = { n -> n * 11 },
        test = 3,
        throwToIfTrue = 3,
        throwToIfFalse = 2
    ),
    Monkey(
        id = 2,
        items = mutableListOf(57, 94, 69, 79, 72),
        operation = { n -> n + 6 },
        test = 19,
        throwToIfTrue = 0,
        throwToIfFalse = 4
    ),
    Monkey(
        id = 3,
        items = mutableListOf(80, 64, 92, 93, 64, 56),
        operation = { n -> n + 5 },
        test = 7,
        throwToIfTrue = 2,
        throwToIfFalse = 0
    ),
    Monkey(
        id = 4,
        items = mutableListOf(70, 88, 95, 99, 78, 72, 65, 94),
        operation = { n -> n + 7 },
        test = 2,
        throwToIfTrue = 7,
        throwToIfFalse = 5
    ),
    Monkey(
        id = 5,
        items = mutableListOf(57, 95, 81, 61),
        operation = { n -> n * n },
        test = 5,
        throwToIfTrue = 1,
        throwToIfFalse = 6
    ),
    Monkey(
        id = 6,
        items = mutableListOf(79, 99),
        operation = { n -> n + 2 },
        test = 11,
        throwToIfTrue = 3,
        throwToIfFalse = 1
    ),
    Monkey(
        id = 7,
        items = mutableListOf(68, 98, 62),
        operation = { n -> n + 3 },
        test = 13,
        throwToIfTrue = 5,
        throwToIfFalse = 6
    ),
)

fun main() {

    println(part1())
    println(part2())
}

fun part1(): Long {
//    val monkeys = testMonkeys()
    val monkeys = prdMonkeys()
    repeat(20) { playRound(monkeys) { item -> item / 3 } }

    return monkeys.sortedByDescending { it.inspectionCount }.take(2).map { it.inspectionCount }.reduce((Long::times))
}

fun part2(): Long {
//    val monkeys = testMonkeys()
    val monkeys = prdMonkeys()

    val lowestCommonMultiple = monkeys.map { it.test.toLong() }.reduce(Long::times)
    repeat(10000) {
        playRound(monkeys) { item -> item % lowestCommonMultiple }
    }

    return monkeys.sortedByDescending { it.inspectionCount }.take(2).map { it.inspectionCount }.reduce((Long::times))
}

private fun playRound(monkeys: List<Monkey>, manageWorryLevel: (Long) -> Long) {
    monkeys.forEach { monkey ->
        repeat(monkey.items.size) {
            val (nexMonkey, newItem) = monkey.inspectItem { item -> manageWorryLevel(item) }
            monkeys[nexMonkey].catchItem(newItem)
        }
    }
}

private data class Monkey(
    val id: Int,
    val items: MutableList<Long>,
    val operation: (Long) -> Long,
    val test: Int,
    val throwToIfFalse: Int,
    val throwToIfTrue: Int,
) {
    var inspectionCount: Long = 0

    fun catchItem(item: Long) {
        items.add(item)
    }

    fun inspectItem(manageWorryLevel: (Long) -> Long): Pair<Int, Long> =
        next(items.first()) { item -> manageWorryLevel(item) }
            .also {
                items.removeAt(0)
                inspectionCount++
            }

    fun next(item: Long, manageWorryLevel: (Long) -> Long): Pair<Int, Long> {
        val newWorryLevel = manageWorryLevel(operation(item))
        return if ((newWorryLevel % test) == 0L)
            throwToIfTrue to newWorryLevel else
            throwToIfFalse to newWorryLevel
    }
}

private fun testMonkeys() = mutableListOf(
    Monkey(
        id = 0,
        items = mutableListOf(79, 98),
        operation = { n -> n * 19 },
        test = 23,
        throwToIfTrue = 2,
        throwToIfFalse = 3
    ),
    Monkey(
        id = 1,
        items = mutableListOf(54, 65, 75, 74),
        operation = { n -> n + 6 },
        test = 19,
        throwToIfTrue = 2,
        throwToIfFalse = 0
    ),
    Monkey(
        id = 2,
        items = mutableListOf(79, 60, 97),
        operation = { n -> n * n },
        test = 13,
        throwToIfTrue = 1,
        throwToIfFalse = 3
    ),
    Monkey(
        id = 3,
        items = mutableListOf(74),
        operation = { n -> n + 3 },
        test = 17,
        throwToIfTrue = 0,
        throwToIfFalse = 1,
    )
)
