package day08

import readInput

typealias Forest = List<Tree>

fun main() {
    val input = readInput("main/day08/Day08")

    val forest = input.flatMapIndexed { y, line ->
        line.mapIndexed { x, height ->
            Tree(x, y, height.digitToInt())
        }
    }

    println(part1(forest))
    println(part2(forest))
}

fun part1(forest: Forest): Int {


    return forest.filter {
        it.isHighestFromLeft(forest) || it.isHighestFromRight(forest) || it.isHighestFromTop(forest) || it.isHighestFromBottom(forest)
    }.distinct().count()
}

fun part2(forest: Forest): Int {

    val treeMap = forest.associateBy { Point(it.x, it.y) }
    val scores = forest.map {
        Point(it.x, it.y) to it.scenicScore(treeMap)
    }
    return scores.maxBy { it.second }.also {
        println(it.first)
    }.second
}

data class Tree(val x: Int, val y: Int, val height: Int) {
    operator fun compareTo(other: Tree) = this.height.compareTo(other.height)

    override fun equals(other: Any?): Boolean {
        return if (other is Tree) x == other.x && y == other.y else false
    }

    override fun hashCode(): Int {
        return x.hashCode() * 31 + y.hashCode()
    }

    fun isHighestFromLeft(otherTrees: Forest): Boolean = otherTrees.none { it.y == y && it >= this && it.x < this.x }

    fun isHighestFromRight(otherTrees: Forest): Boolean = otherTrees.none { it.y == y && it >= this && it.x > this.x }

    fun isHighestFromTop(otherTrees: Forest): Boolean = otherTrees.none { it.x == x && it >= this && it.y < this.y }

    fun isHighestFromBottom(otherTrees: Forest): Boolean = otherTrees.none { it.x == x && it >= this && it.y > this.y }

    fun scenicScore(treeMap: Map<Point, Tree>): Int {
        return visibleTreesLeft(treeMap) * visibleTreesDown(treeMap) * visibleTreesRight(treeMap) * visibleTreesUp(treeMap)
    }

    private fun visibleTreesLeft(treeMap: Map<Point, Tree>): Int {
        var result = 0
        var xpos = x - 1
        while (xpos >= 0 && treeMap[Point(xpos, y)]!! < this) {
            result++
            xpos--

        }
        return result + if (xpos < 0) 0 else 1
    }

    private fun visibleTreesRight(treeMap: Map<Point, Tree>): Int {
        val maxX = treeMap.maxOf { it.key.x }
        var result = 0
        var xpos = x + 1
        while (xpos <= maxX && treeMap[Point(xpos, y)]!! < this) {
            result++
            xpos++
        }
        return result + if (xpos > maxX) 0 else 1
    }

    private fun visibleTreesUp(treeMap: Map<Point, Tree>): Int {
        var result = 0
        var ypos = y - 1
        while (ypos >= 0 && treeMap[Point(x, ypos)]!! < this) {
            result++
            ypos--
        }
        return result + if (ypos < 0) 0 else 1
    }

    private fun visibleTreesDown(treeMap: Map<Point, Tree>): Int {
        val maxY = treeMap.maxOf { it.key.y }
        var result = 0
        var ypos = y + 1
        while (ypos <= maxY && treeMap[Point(x, ypos)]!! < this) {
            result++
            ypos++
        }
        return result + if (ypos > maxY) 0 else 1
    }
}

data class Point(val x: Int, val y: Int)
