package day07

import readInput

private const val THRESHOLD_PART1 = 100000
private const val TOTAL_DISC_SIZE = 70000000
private const val SIZE_FOR_UPDATE = 30000000

class Directory(
    val name: String,
    val files: MutableSet<Int>,
    val dirs: MutableMap<String, Directory>,
    val parent: Directory? = null
) {
    val totalSize: Int
        get() = files.sum() + dirs.values.sumOf { it.totalSize }

    fun allDirectories(): List<Directory> = listOf(this) + dirs.values.flatMap { it.allDirectories() }
    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Directory) name == other.name else false
    }
}

fun main() {
    val input = readInput("main/day07/Day07")
    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Int {
    val root = input.parseDirectories()

    return root.allDirectories()
        .filter { it.totalSize <= THRESHOLD_PART1 }
        .sumOf { it.totalSize }
}

fun part2(input: List<String>): Int {
    val root = input.parseDirectories()

    return root.allDirectories().filter {
        it.totalSize >= SIZE_FOR_UPDATE - (TOTAL_DISC_SIZE - root.totalSize)
    }.minBy { it.totalSize }.totalSize
}

fun List<String>.parseDirectories(): Directory {

    val root = Directory("/", mutableSetOf(), mutableMapOf())
    var currentDirectory = root
    forEach { line ->
        when {
            line.isCd() -> currentDirectory = cd(currentDirectory, line.lastArg(), root)
            line.isDir() -> currentDirectory.dirs[line.lastArg()] = Directory(line.lastArg(), mutableSetOf(), mutableMapOf(), currentDirectory)
            line.isFile() -> currentDirectory.files.add(line.firstArg().toInt())
        }
    }
    return root
}

private fun cd(currentDirectory: Directory, arg: String, root: Directory): Directory {
    return when (arg) {
        "/" -> root
        ".." -> currentDirectory.parent ?: root
        else -> currentDirectory.dirs[arg] ?: Directory(arg, mutableSetOf(), mutableMapOf(), currentDirectory)
    }
}

private fun String.lastArg() = split(" ").last()
private fun String.firstArg() = split(" ").first()

fun String.isCd() = startsWith("$ cd")
fun String.isFile() = first().isDigit()
fun String.isDir() = startsWith("dir")