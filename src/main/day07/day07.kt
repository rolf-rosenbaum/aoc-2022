package day07

import readInput

private const val THRESHOLD_PART1 = 100000
private const val TOTAL_DISC_SIZE = 70000000
private const val SIZE_FOR_UPDATE = 30000000

data class Directory(
    val name: String,
    val files: MutableSet<Int>,
    val dirs: MutableMap<String, Directory>,
    val parent: Directory? = null
) {
    val totalSize: Int
        get() = files.sum() + dirs.values.sumOf { it.totalSize }

    val allDirectories: List<Directory>
        get() = listOf(this) + dirs.values.flatMap { it.allDirectories }

    private fun root(): Directory = parent?.root() ?: this

    fun changeInto(arg: String): Directory {
        return when (arg) {
            "/" -> root()
            ".." -> parent ?: root()
            else -> dirs[arg] ?: Directory(arg, mutableSetOf(), mutableMapOf(), this)
        }
    }

    override fun hashCode(): Int = name.hashCode()

    override fun equals(other: Any?): Boolean = if (other is Directory) name == other.name else false
}

fun main() {
    val input = readInput("main/day07/Day07")
    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Int {

    return input.parseDirectories().allDirectories
        .filter { it.totalSize <= THRESHOLD_PART1 }
        .sumOf { it.totalSize }
}

fun part2(input: List<String>): Int {

    val root = input.parseDirectories()

    return root.allDirectories.filter {
        it.totalSize >= SIZE_FOR_UPDATE - (TOTAL_DISC_SIZE - root.totalSize)
    }.minBy { it.totalSize }.totalSize
}

fun List<String>.parseDirectories(): Directory {

    val root = Directory("/", mutableSetOf(), mutableMapOf())
    var current = root
    forEach { line ->
        when {
            line.isCd() -> current = current.changeInto(line.lastArg())
            line.isDir() -> current.dirs[line.lastArg()] = Directory(line.lastArg(), mutableSetOf(), mutableMapOf(), current)
            line.isFile() -> current.files.add(line.firstArg().toInt())
        }
    }
    return root
}

private fun String.lastArg() = split(" ").last()
private fun String.firstArg() = split(" ").first()

fun String.isCd() = startsWith("$ cd")
fun String.isFile() = first().isDigit()
fun String.isDir() = startsWith("dir")
