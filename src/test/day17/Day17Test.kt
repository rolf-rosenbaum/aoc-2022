package day17

import org.junit.jupiter.api.Test
import readInput
import second

class Day17Test {
    
    @Test
    fun patternSearch() {
        val input = readInput("main/day17/patterntest")

        val heights = input.map { it.toInt() }

        val diffs = heights.windowed(2).map { it.second() - it.first() }
        diffs.forEach { println(it) }

    }
}