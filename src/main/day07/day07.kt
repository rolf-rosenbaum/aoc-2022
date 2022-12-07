package day07

import readInput

class Directory(
    val name: String,
    val files: MutableSet<Int>,
    val dirs: MutableMap<String, Directory>,
    val parent: Directory? = null
) {
    fun totalSize(): Int = files.sum() + dirs.values.sumOf { it.totalSize() }
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
    val root = input.parse()

    return root.allDirectories()
        .filter { it.totalSize() <= 100000 }
        .sumOf { it.totalSize() }
}

fun part2(input: List<String>): Int {

    val root = input.parse()
    val foo = 70000000 - root.totalSize()

    return root.allDirectories().filter { it.totalSize() >= 30000000 - foo }.minBy { it.totalSize() }.totalSize()
}

fun List<String>.parse(): Directory {
    val root = Directory("/", mutableSetOf(), mutableMapOf())
    var currentDirectory = root
    forEach { line ->
        if (line.isCd()) {
            currentDirectory = cd(currentDirectory, line, root)
        } else if (line.isDir()) {
            currentDirectory.dirs[line.arg()] = Directory(line.arg(), mutableSetOf(), mutableMapOf(), currentDirectory)
        } else if (line.isFile()) {
            currentDirectory.files.add(line.split(" ").first().toInt())
        }
    }

    return root
}

private fun cd(currentDirectory: Directory, line: String, root: Directory): Directory {
    var currentDirectory1 = currentDirectory
    return if (line.arg() == "/") {
        root
    } else if (line.arg() == "..") {
        currentDirectory1.parent ?: root
    } else {
        val name = line.arg()
        val d = currentDirectory1.dirs[name] ?: Directory(name, mutableSetOf(), mutableMapOf(), currentDirectory1)
        d
    }
}

private fun String.arg() = split(" ").last()

fun String.isCd() = startsWith("$ cd")
fun String.isFile() = first().isDigit()
fun String.isDir() = startsWith("dir")