package day08

import Point
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
    return forest.count {
        it.isVisibleFromLeft(forest) || it.isVisibleFromRight(forest) || it.isVisibleFromTop(forest) || it.isVisibleFromBottom(forest)
    }
}

fun part2(forest: Forest): Int {
    return forest.maxOf {
        it.scenicScore(forest.associateBy { tree -> Point(tree.x, tree.y) })
    }
}

data class Tree(val x: Int, val y: Int, val height: Int) {
    operator fun compareTo(other: Tree) = this.height.compareTo(other.height)

    override fun equals(other: Any?): Boolean {
        return if (other is Tree) x == other.x && y == other.y else false
    }

    override fun hashCode(): Int {
        return x.hashCode() * 31 + y.hashCode()
    }

    fun isVisibleFromLeft(otherTrees: Forest): Boolean = otherTrees.none { it.y == y && it >= this && it.x < this.x }

    fun isVisibleFromRight(otherTrees: Forest): Boolean = otherTrees.none { it.y == y && it >= this && it.x > this.x }

    fun isVisibleFromTop(otherTrees: Forest): Boolean = otherTrees.none { it.x == x && it >= this && it.y < this.y }

    fun isVisibleFromBottom(otherTrees: Forest): Boolean = otherTrees.none { it.x == x && it >= this && it.y > this.y }

    fun scenicScore(treeMap: Map<Point, Tree>): Int {
        return countVisibleTreesLeft(treeMap) * countVisibleTreesDown(treeMap) * countVisibleTreesRight(treeMap) * countVisibleTreesUp(treeMap)
    }

    private fun countVisibleTreesLeft(treeMap: Map<Point, Tree>): Int {
        var result = 0
        var xpos = x - 1
        while (xpos >= 0 && this > treeMap[Point(xpos, y)]!!) {
            result++
            xpos--
        }
        return result + if (xpos < 0) 0 else 1
    }

    private fun countVisibleTreesRight(treeMap: Map<Point, Tree>): Int {
        val maxX = treeMap.maxOf { it.key.x }
        var result = 0
        var xpos = x + 1
        while (xpos <= maxX && this > treeMap[Point(xpos, y)]!!) {
            result++
            xpos++
        }
        return result + if (xpos > maxX) 0 else 1
    }

    private fun countVisibleTreesUp(treeMap: Map<Point, Tree>): Int {
        var result = 0
        var ypos = y - 1
        while (ypos >= 0 && this > treeMap[Point(x, ypos)]!!) {
            result++
            ypos--
        }
        return result + if (ypos < 0) 0 else 1
    }

    private fun countVisibleTreesDown(treeMap: Map<Point, Tree>): Int {
        val maxY = treeMap.maxOf { it.key.y }
        var result = 0
        var ypos = y + 1
        while (ypos <= maxY && this > treeMap[Point(x, ypos)]!!) {
            result++
            ypos++
        }
        return result + if (ypos > maxY) 0 else 1
    }
}


