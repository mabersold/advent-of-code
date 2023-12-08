package puzzles.year2023

import puzzles.Puzzle

class Day3: Puzzle(2023, 3, "Gear Ratios") {
    override fun part1(): Any {
        val input = getInput(1)

        val partNumbers = mutableListOf<Int>()

        input.forEachIndexed { row, line ->
            val builder = StringBuilder()
            var hasAdjacentSymbol = false

            for (i in line.indices) {
                if (line[i].isDigit()) {
                    // check for surrounding symbols
                    builder.append(line[i])
                    if (!hasAdjacentSymbol)
                        hasAdjacentSymbol = hasAdjacentSymbol(row, i, input)
                }

                if (!line[i].isDigit() || i == line.length - 1){
                    // check if builder is empty
                    if (builder.isNotEmpty()) {
                        val number = builder.toString().toInt()
                        if (hasAdjacentSymbol) {
                            partNumbers.add(number)
                        }

                        builder.clear()
                        hasAdjacentSymbol = false
                    }
                }
            }
        }

        return partNumbers.sum()
    }

    override fun part2(): Any {
        val input = getInput(1)

        val gears = hashMapOf<Pair<Int, Int>, MutableList<Int>>()

        input.forEachIndexed { row, line ->
            val builder = StringBuilder()
            var adjacentGear: Pair<Int, Int>?  = null

            for (i in line.indices) {
                if (line[i].isDigit()) {
                    // check for surrounding symbols
                    builder.append(line[i])
                    if (adjacentGear == null) {
                        adjacentGear = getAdjacentGear(row, i, input)
                    }
                }

                if (!line[i].isDigit() || i == line.length - 1){
                    // check if builder is empty
                    if (builder.isNotEmpty()) {
                        val number = builder.toString().toInt()
                        if (adjacentGear != null) {
                            if (!gears.containsKey(adjacentGear)) {
                                gears[adjacentGear] = mutableListOf(number)
                            } else {
                                gears[adjacentGear]?.add(number)
                            }
                        }

                        builder.clear()
                        adjacentGear = null
                    }
                }
            }
        }

        return gears.values.filter { it.size == 2 }.sumOf { it[0] * it[1] }
    }

    private fun hasAdjacentSymbol(row: Int, column: Int, values: List<String>): Boolean {
        val coordinatesToCheck = getCoordinatesToCheck(row, column, values.size - 1, values.first().length - 1)

        return coordinatesToCheck.any { coordinates ->
            val character = values[coordinates.first][coordinates.second]
            !character.isDigit() && character != '.'
        }
    }

    private fun getAdjacentGear(row: Int, column: Int, values: List<String>): Pair<Int, Int>? {
        val coordinatesToCheck = getCoordinatesToCheck(row, column, values.size - 1, values.first().length - 1)

        return coordinatesToCheck.firstOrNull { coordinates ->
            values[coordinates.first][coordinates.second] == '*'
        }
    }

    private fun getCoordinatesToCheck(row: Int, column: Int, maxRows: Int, maxCols: Int): HashSet<Pair<Int, Int>> {
        val coordinatesToCheck = hashSetOf<Pair<Int, Int>>()

        if (row > 0) {
            coordinatesToCheck.add(Pair(row - 1, column))
            if (column > 0) {
                coordinatesToCheck.add(Pair(row - 1, column - 1))
            }
            if (column < maxCols) {
                coordinatesToCheck.add(Pair(row - 1, column + 1))
            }
        }

        if (row < maxRows) {
            coordinatesToCheck.add(Pair(row + 1, column))
            if (column > 0) {
                coordinatesToCheck.add(Pair(row + 1, column - 1))
            }
            if (column < maxCols) {
                coordinatesToCheck.add(Pair(row + 1, column + 1))
            }
        }

        if (column > 0) {
            coordinatesToCheck.add(Pair(row, column - 1))
        }

        if (column < maxCols) {
            coordinatesToCheck.add(Pair(row, column + 1))
        }

        return coordinatesToCheck
    }
}