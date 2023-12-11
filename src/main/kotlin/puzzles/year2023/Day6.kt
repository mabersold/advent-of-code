package puzzles.year2023

import puzzles.Puzzle
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

class Day6: Puzzle(2023, 6, "Wait For It") {
    override fun part1(): Any {
        val input = getInput(1)

        val times = input[0].removePrefix("Time:").trim().split(Regex("\\s+")).map { it.toLong() }
        val distances = input[1].removePrefix("Distance:").trim().split(Regex("\\s+")).map { it.toLong() }

        var result = 1L

        for (i in times.indices) {
            val b = times[i]
            val c = distances[i]

            result *= solve(b, c)
        }

        return result
    }

    override fun part2(): Any {
        val input = getInput(1)

        val times = input[0].filter { it.isDigit() }.toLong()
        val distances = input[1].filter { it.isDigit() }.toLong()

        return solve(times, distances)
    }

    private fun solve(time: Long, distance: Long): Long {
        val b = time / 2.0
        val discriminant = sqrt(b * b - distance)
        return (ceil(b + discriminant - 1) - floor(b - discriminant + 1) + 1).toLong()
    }
}