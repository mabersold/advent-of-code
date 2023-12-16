package puzzles.year2023

import puzzles.Puzzle

class Day14: Puzzle(2023, 14, "Parabolic Reflector Dish") {
    override fun part1(): Any {
        val input = getInput(1)
        val weights = IntArray(input.first().length) { input.size }

        var sum = 0

        input.forEachIndexed { rowNumber, rowData ->
            for (col in rowData.indices) {
                if (rowData[col] == 'O') {
                    sum += weights[col]
                    weights[col]--
                } else if (rowData[col] == '#') {
                    weights[col] = input.size - (rowNumber + 1)
                }
            }
        }

        return sum
    }

    override fun part2(): Any {
        val input = getInput(1)

        val charArray = Array(input.size) { CharArray(input.first().length) { '.' } }

        input.forEachIndexed { index, s ->
            charArray[index] = s.toCharArray()
        }

        val grid = Grid(charArray)

        // 97052 is too low
        // 96817 is too low
        return grid.shiftTimes(1000000000)
    }

    data class Grid(val grid: Array<CharArray>) {
        private val allBarriers: Set<Pair<Int, Int>>
        private val northStartingPoints: Set<Pair<Int, Int>>
        private val leftStartingPoints: Set<Pair<Int, Int>>
        private val southStartingPoints: Set<Pair<Int, Int>>
        private val rightStartingPoints: Set<Pair<Int, Int>>
        private val configurationTurns = hashMapOf<String, Int>()
        private val answers = hashMapOf<Int, Int>()

        init {
            allBarriers = mutableSetOf()

            for (row in grid.indices) {
                for (col in grid.first().indices) {
                    if (grid[row][col] == '#') {
                        allBarriers.add(Pair(row, col))
                    }
                }
            }

            val allTopRow = grid.first().indices.filter { grid.first()[it] != '#' }.map { Pair(0, it) }.toSet()
            val allSpotsSouthOfBarriers = allBarriers.filter { it.first < grid.size - 1 && !allBarriers.contains(Pair(it.first + 1, it.second)) }
                .map { Pair(it.first + 1, it.second) }.toSet()

            northStartingPoints = allTopRow + allSpotsSouthOfBarriers

            val allLeftRow = grid.indices.filter { grid[it][0] != '#' }.map { Pair(it, 0) }.toSet()
            val allSpotsRightOfBarriers = allBarriers.filter { it.second < grid.first().size - 1 && !allBarriers.contains(Pair(it.first, it.second + 1)) }
                .map { Pair(it.first, it.second + 1) }.toSet()

            leftStartingPoints = allLeftRow + allSpotsRightOfBarriers

            val allBottomRow = grid.last().indices.filter { grid.last()[it] != '#' }.map { Pair(grid.size - 1, it) }.toSet()
            val allSpotsNorthOfBarriers = allBarriers.filter { it.first > 0 && !allBarriers.contains(Pair(it.first - 1, it.second)) }
                .map { Pair(it.first - 1, it.second) }.toSet()

            southStartingPoints = allBottomRow + allSpotsNorthOfBarriers

            val allRightRow = grid.indices.filter { grid[it].last() != '#' }.map { Pair(it, grid.last().size - 1) }.toSet()
            val allSpotsLeftOfBarriers = allBarriers.filter { it.second > 0 && !allBarriers.contains(Pair(it.first, it.second - 1)) }
                .map { Pair(it.first, it.second - 1) }.toSet()

            rightStartingPoints = allRightRow + allSpotsLeftOfBarriers
        }

        private fun getAnswer(): Int {
            var answer = 0

            for (row in grid.indices) {
                for (col in grid[row].indices) {
                    if (grid[row][col] == 'O') {
                        answer += grid.size - row
                    }
                }
            }

            return answer
        }

        fun shiftTimes(times: Int): Int {
            var totalShifts = 0

            while (totalShifts < times) {
                val previousGridString = getGridString()
                shiftUp()
                shiftLeft()
                shiftDown()
                shiftRight()
                totalShifts++

                val gridString = getGridString()

                if (configurationTurns.containsKey(gridString)) {
                    val sequenceStart = configurationTurns[gridString]
                    val sequenceEnd = configurationTurns[previousGridString]

                    val calculatedIndex = sequenceStart!! + (times - sequenceStart) % (sequenceEnd!! - sequenceStart + 1)

                    return answers[calculatedIndex]!!
                } else {
                    // we have not identified the sequence, so let's store some info
                    configurationTurns[gridString] = totalShifts
                    answers[totalShifts] = getAnswer()
                }
            }

            return getAnswer()
        }

        private fun getGridString(): String {
            val builder = StringBuilder()
            for (row in grid.indices) {
                for (col in grid[row].indices) {
                    if (grid[row][col] == 'O') {
                        builder.append("$row,$col;")
                    }
                }
            }

            return builder.toString()
        }

        private fun shiftUp() {
            for (startingPoint in northStartingPoints) {
                var shiftRow = startingPoint.first

                var counter = startingPoint.first
                val counterMax = grid.size - 1

                while (counter <= counterMax && grid[counter][startingPoint.second] != '#') {
                    if (grid[counter][startingPoint.second] == 'O') {
                        grid[counter][startingPoint.second] = '.'
                        grid[shiftRow][startingPoint.second] = 'O'
                        shiftRow++
                    }

                    counter++
                }
            }
        }

        private fun shiftLeft() {
            for (startingPoint in leftStartingPoints) {
                var shiftColumn = startingPoint.second

                var counter = startingPoint.second
                val counterMax = grid.first().size - 1

                while (counter <= counterMax && grid[startingPoint.first][counter] != '#') {
                    if (grid[startingPoint.first][counter] == 'O') {
                        grid[startingPoint.first][counter] = '.'
                        grid[startingPoint.first][shiftColumn] = 'O'
                        shiftColumn++
                    }

                    counter++
                }
            }
        }

        private fun shiftDown() {
            for (startingPoint in southStartingPoints) {
                var shiftRow = startingPoint.first

                var counter = startingPoint.first
                val counterMin = 0

                while (counter >= counterMin && grid[counter][startingPoint.second] != '#') {
                    if (grid[counter][startingPoint.second] == 'O') {
                        grid[counter][startingPoint.second] = '.'
                        grid[shiftRow][startingPoint.second] = 'O'
                        shiftRow--
                    }

                    counter--
                }
            }
        }

        private fun shiftRight() {
            for (startingPoint in rightStartingPoints) {
                var shiftColumn = startingPoint.second
                var counter = startingPoint.second
                val counterMin = 0

                while (counter >= counterMin && grid[startingPoint.first][counter] != '#') {
                    if (grid[startingPoint.first][counter] == 'O') {
                        grid[startingPoint.first][counter] = '.'
                        grid[startingPoint.first][shiftColumn] = 'O'
                        shiftColumn--
                    }

                    counter --
                }
            }
        }
    }
}