package year_2016.day18

const val input: Row = ".^^.^^^..^.^..^.^^.^^^^.^^.^^...^..^...^^^..^^...^..^^^^^^..^.^^^..^.^^^^.^^^.^...^^^.^^.^^^.^.^^.^."

typealias Row = String

fun main() {
    println(part1())
    println(part2())

}

fun part1(): Int = generateSequence(input) { s -> s.nextRow() }.take(40).sumOf { it.count { c -> c == '.' } }

fun part2(): Int = generateSequence(input) { s -> s.nextRow() }.take(400000).sumOf { it.count { c -> c == '.' } }

fun Row.nextRow(): Row {
    return this.mapIndexed { index, c ->
        if (leftIsTrap(index) && centerIsTrap(index) && !rightIsTrap(index)) '^'
        else if (!leftIsTrap(index) && centerIsTrap(index) && rightIsTrap(index)) '^'
        else if (leftIsTrap(index) && !centerIsTrap(index) && !rightIsTrap(index)) '^'
        else if (!leftIsTrap(index) && !centerIsTrap(index) && rightIsTrap(index)) '^'
        else '.'
    }.joinToString("")
}

fun Row.leftIsTrap(i: Int) = i > 0 && this[i - 1] == '^'
fun Row.rightIsTrap(i: Int) = i < length - 1 && this[i + 1] == '^'
fun Row.centerIsTrap(i: Int) = this[i] == '^'

