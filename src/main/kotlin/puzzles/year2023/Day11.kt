package puzzles.year2023

import puzzles.Puzzle

class Day11: Puzzle(2023, 11, "Cosmic Expansion") {
    override fun part1(): Any {
        val input = getInput(1)
        return getAnswer(input)
    }

    override fun part2(): Any {
        val input = getInput(1)
        return getAnswer(input, 999999)
    }

    /**
     * This one is pretty easy, it's just a different parameter between the two puzzles.
     * So I implemented most of it in this private function.
     */
    private fun getAnswer(input: List<String>, multiplicationFactor: Int = 1): Long {
        // Row numbers that have no galaxies in them
        val expandedRows = mutableSetOf<Int>()
        // Column numbers that have no galaxies in them
        val expandedColumns = mutableSetOf<Int>()
        // All x-y coordinates that have a galaxy
        val galaxyCoordinates = arrayListOf<Pair<Int, Int>>()

        // This is needed to help build the expandedColumns set - we could also build it by doing another loop, but
        // this avoids another loop by doing so
        val columnsWithGalaxies = Array(input.first().length) { false }

        for (row in input.indices) {
            var rowHasGalaxies = false
            for (column in input[row].indices) {
                if (input[row][column] == '#') {
                    // It's a galaxy! Put this into our galaxyCoordinates list, and indicate that this row (and column) has galaxies
                    rowHasGalaxies = true
                    galaxyCoordinates.add(Pair(row, column))
                    columnsWithGalaxies[column] = true
                }
            }
            if (!rowHasGalaxies) {
                // If we haven't found any galaxies in a row, add it to our expanded rows set
                expandedRows.add(row)
            }
        }

        // We now have the information we need to build our expanded columns set, which requires no additional nested loops
        for (column in columnsWithGalaxies.indices) {
            if (!columnsWithGalaxies[column]) {
                expandedColumns.add(column)
            }
        }

        /**
         * Use two for loops to make a cartesian product of all the galaxy coordinates. For each pair, find the distance.
         * The multiplaction factor is the only difference between the two examples. In part 1, it's 1. In part 2, it's 999999
         */
        var totalDistances = 0L
        for (counter1 in galaxyCoordinates.indices) {
            for (counter2 in counter1 + 1..<galaxyCoordinates.size) {
                totalDistances += getDistance(galaxyCoordinates[counter1], galaxyCoordinates[counter2], expandedRows, expandedColumns, multiplicationFactor)
            }
        }

        return totalDistances
    }

    private fun getDistance(galaxy1: Pair<Int, Int>, galaxy2: Pair<Int, Int>, expandedRows: Set<Int>, expandedColumns: Set<Int>, multiplicationFactor: Int = 1): Long {
        val greaterRow = maxOf(galaxy1.first, galaxy2.first).toLong()
        val lesserRow = minOf(galaxy1.first, galaxy2.first)

        val expandedRowsInRange = expandedRows.count { it in (lesserRow + 1)..<greaterRow }

        val greaterColumn = maxOf(galaxy1.second, galaxy2.second)
        val lesserColumn = minOf(galaxy1.second, galaxy2.second)
        val expandedColumnsInRange = expandedColumns.count { it in (lesserColumn + 1)..<greaterColumn }

        // Really simple - just find the Manhattan distance between the two, and include the multiplication factor
        return greaterRow - lesserRow + (multiplicationFactor * expandedRowsInRange) + greaterColumn - lesserColumn + (multiplicationFactor * expandedColumnsInRange)
    }
}