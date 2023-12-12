package puzzles.year2023

import puzzles.Puzzle

class Day9: Puzzle(2023, 9, "Mirage Maintenance") {
    override fun part1(): Any {
        val input = getInput(1)

        return input.sumOf { i ->
            var sequence = i.split(" ").map { it.toInt() }
            val lastNumberStack = arrayListOf<Int>()

            while(!sequence.isAllZeroes()) {
                lastNumberStack.add(sequence.last())
                sequence = sequence.generateNewSequence()
            }

            var runningTotal = 0
            for (i2 in lastNumberStack.indices.reversed()) {
                runningTotal += lastNumberStack[i2]
            }

            runningTotal
        }
    }

    override fun part2(): Any {
        val input = getInput(1)

        return input.sumOf { i ->
            var sequence = i.split(" ").map { it.toInt() }
            val firstNumberStack = arrayListOf<Int>()

            while(!sequence.isAllZeroes()) {
                firstNumberStack.add(sequence.first())
                sequence = sequence.generateNewSequence()
            }

            var runningTotal = 0
            for (i2 in firstNumberStack.indices.reversed()) {
                runningTotal = firstNumberStack[i2] - runningTotal
            }

            runningTotal
        }
    }

    private fun List<Int>.isAllZeroes(): Boolean =
        this.all { it == 0 }

    private fun List<Int>.generateNewSequence(): List<Int> =
        this.windowed(2, 1, false).map { w ->
            w[1] - w[0]
        }
}