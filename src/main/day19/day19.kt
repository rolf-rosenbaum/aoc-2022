package day19

import day19.Resource.CLAY
import day19.Resource.GEODE
import day19.Resource.NONE
import day19.Resource.OBSIDIAN
import day19.Resource.ORE
import kotlin.math.max
import readInput

val regex = """.+ (\d+).+ (\d+) .+ (\d+) .+ (\d+) .+ (\d+) .+ (\d+).+ (\d+) .*""".toRegex()

fun main() {
    val input = readInput("main/day19/Day19_test")
    println(part1(input))
    println(part2(input))
}

fun part1(input: List<String>): Int {
    val bluePrints = input.toBlueprints()
    return bluePrints.sumOf { it.id * it.maxGeodes(0) }
}

fun part2(input: List<String>): Long {
    return -1
}

enum class Resource {
    NONE, ORE, CLAY, OBSIDIAN, GEODE
}

fun List<String>.toBlueprints() = map { line ->
    regex.matchEntire(line)?.destructured?.let { (id, oreRobotOre, clayRoboTOre, obsidianRobotOre, obsidianRobotClay, geodeRobotOre, geodeRobotObsidian) ->
        BluePrint(
            id = id.toInt(),
            oreRobotTemplate = OreRobot(cost = listOf(Cost(ORE, oreRobotOre.toInt()))),
            clayRobotTemplate = ClayRobot(cost = listOf(Cost(ORE, clayRoboTOre.toInt()))),
            obsidianRobotTemplate = ObsidianRobot(cost = listOf(Cost(ORE, obsidianRobotOre.toInt()), Cost(CLAY, obsidianRobotClay.toInt()))),
            geodeRobotTemplate = GeodeRobot(cost = listOf(Cost(ORE, geodeRobotOre.toInt()), Cost(OBSIDIAN, geodeRobotObsidian.toInt()))),
        )
    } ?: error("PARSER ERROR")
}

data class Cost(val resource: Resource, val amount: Int)
data class BluePrint(
    val id: Int,
    val oreRobotTemplate: OreRobot,
    val clayRobotTemplate: ClayRobot,
    val obsidianRobotTemplate: ObsidianRobot,
    val geodeRobotTemplate: GeodeRobot
) {
    private val allTemplates = listOf(oreRobotTemplate, clayRobotTemplate, obsidianRobotTemplate, geodeRobotTemplate).reversed()

    fun maxGeodes(
        maxSoFar: Int,
        robots: List<Robot> = listOf(oreRobotTemplate.instance()),
        resources: MutableList<Resource> = mutableListOf(),
        timeLeft: Int = 24
    ): Int {
        if (timeLeft > 0) {
            val buildableRobots = buildableRobots(resources)
            buildableRobots.forEach { newRobot ->
                newRobot.cost.forEach { repeat(it.amount) { _ -> resources.remove(it.resource) } }
                return maxGeodes(
                    max(maxSoFar, resources.count { it == GEODE }),
                    robots + newRobot,
                    resources.filter { it != NONE }.toMutableList(),
                    timeLeft - 1
                )
            }
            robots.forEach { resources.add(it.resource) }
            return maxGeodes(max(maxSoFar, resources.count { it == GEODE }), robots, resources, timeLeft - 1)
        } else return maxSoFar
    }

    private fun buildableRobots(resources: List<Resource>): MutableList<Robot> {
        return allTemplates.filter {
            it.canBeBuiltFrom(resources)
        }.map {
            it.instance()
        }.toMutableList()
    }
}

sealed interface Robot {
    val resource: Resource
    val cost: List<Cost>
    fun canBeBuiltFrom(resources: List<Resource>): Boolean
    fun instance(): Robot
}

data class OreRobot(override val resource: Resource = ORE, override val cost: List<Cost>) : Robot {
    override fun canBeBuiltFrom(resources: List<Resource>): Boolean {
        return cost.all { cost ->
            resources.count {
                it == cost.resource
            } >= cost.amount
        }
    }

    override fun instance(): Robot {
        return copy()
    }
}

data class ClayRobot(override val resource: Resource = CLAY, override val cost: List<Cost>) : Robot {
    override fun canBeBuiltFrom(resources: List<Resource>): Boolean {
        return cost.all { cost ->
            resources.count {
                it == cost.resource
            } >= cost.amount
        }
    }

    override fun instance(): Robot {
        return copy()
    }
}

data class ObsidianRobot(override val resource: Resource = OBSIDIAN, override val cost: List<Cost>) : Robot {
    override fun canBeBuiltFrom(resources: List<Resource>): Boolean {
        return cost.all { cost ->
            resources.count {
                it == cost.resource
            } >= cost.amount
        }
    }

    override fun instance(): Robot {
        return copy()
    }
}

data class GeodeRobot(override val resource: Resource = GEODE, override val cost: List<Cost>) : Robot {
    override fun canBeBuiltFrom(resources: List<Resource>): Boolean {
        return cost.all { cost ->
            resources.count {
                it == cost.resource
            } >= cost.amount
        }
    }

    override fun instance(): Robot {
        return copy()
    }
}

//object NoOp : Robot {
//    override val resource: Resource
//        get() = NONE
//    override val cost: List<Cost>
//        get() = emptyList()
//
//    override fun canBeBuiltFrom(resources: List<Resource>): Boolean = true
//
//    override fun instance(): Robot {
//        return this
//    }
//}
