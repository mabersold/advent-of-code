package puzzles.year2023

import models.Direction
import models.Point
import puzzles.Puzzle

class Day18: Puzzle(2023, 18, "Lavaduct Lagoon") {
    override fun part1(): Any {
        val input = getInput(1)

        val instructions = input.map { line ->
            val instruction = line.split(" ")
            val direction = getDirection(instruction[0])
            val distance = instruction[1].toInt()

            val point = when(direction) {
                Direction.UP -> Point(-1, 0)
                Direction.DOWN -> Point(1, 0)
                Direction.RIGHT -> Point(0, 1)
                Direction.LEFT -> Point(0, -1)
            }

            Pair(point, distance)
        }

        return getArea(instructions)
    }

    override fun part2(): Any {
        val input = getInput(1)

        val instructions = input.map { line ->
            val hexCode = line.substringAfter("#").substringBefore(")")
            val direction = getDirection(hexCode.last().toString())
            val distance = hexCode.dropLast(1).toInt(16)

            val point = when(direction) {
                Direction.UP -> Point(-1, 0)
                Direction.DOWN -> Point(1, 0)
                Direction.RIGHT -> Point(0, 1)
                Direction.LEFT -> Point(0, -1)
            }

            Pair(point, distance)
        }

        return getArea(instructions)
    }

    private fun getArea(instructions: List<Pair<Point, Int>>): Long {
        val area = instructions.runningFold(Point(0, 0)) { acc, (direction, distance) ->
            acc + (direction * distance)
        }.zipWithNext().sumOf { (a, b) ->
            (a.column.toLong() * b.row.toLong()) - (a.row.toLong() * b.column.toLong())
        } / 2

        val perimeter = instructions.sumOf { it.second }
        return area + (perimeter / 2) + 1
    }

    private fun getDirection(s: String): Direction {
        return when (s) {
            "U", "3" -> Direction.UP
            "D", "1" -> Direction.DOWN
            "L", "2" -> Direction.LEFT
            else -> Direction.RIGHT
        }
    }
}