package puzzles.year2023

import puzzles.Puzzle

class Day13: Puzzle(2023, 13, "Point of Incidence") {
    override fun part1(): Any {
        val input = getInputAsSingleString(1)

        val patterns = input.split("\n\n")

        return patterns.sumOf { p ->
            val pattern = p.split("\n")

            pattern.getPart1Answer()
        }
    }

    override fun part2(): Any {
        val input = getInputAsSingleString(1)

        val patterns = input.split("\n\n")

        return patterns.sumOf { p ->
            val pattern = p.split("\n")

            pattern.getPart2Answer()
        }
    }

    private fun List<String>.findReflectedRows(): List<Int> {
        val reflectedRowNumbers = mutableListOf<Int>()

        for (r in 0..<this.size - 1) {
            if (this[r] == this[r+1]) {
                reflectedRowNumbers.add(r)
            }
        }

        return reflectedRowNumbers
    }

    private fun List<String>.findReflectedColumns(): List<Int> {
        val reflectedColumnNumbers = mutableListOf<Int>()

        for (c in 0..<this.first().length - 1) {
            if (this.map { it[c] } == this.map { it[c + 1] }) {
                reflectedColumnNumbers.add(c)
            }
        }

        return reflectedColumnNumbers
    }

    private fun List<String>.getPart1Answer(): Int {
        this.findReflectedRows().forEach { row ->
            if (this.hasPerfectRowReflection(row))
                return 100 * (row + 1)
        }

        this.findReflectedColumns().forEach { col ->
            if (this.hasPerfectColumnReflection(col))
                return col + 1
        }

        return 0
    }

    private fun List<String>.getPart2Answer(): Int {
        // Rows
        for (r in 0..<this.size - 1) {
            if (this.hasAlmostPerfectRowReflection(r))
                return 100 * (r + 1)
        }

        for (c in 0..<this.first().length - 1) {
            if (this.hasAlmostPerfectColumnReflection(c))
                return c + 1
        }

        return 0
    }

    private fun List<String>.hasPerfectRowReflection(rowNumber: Int): Boolean {
        var lesser = rowNumber
        var greater = rowNumber + 1

        while (lesser >= 0 && greater <= this.size - 1) {
            if (this[lesser] != this[greater])
                return false

            lesser--
            greater++
        }

        return true
    }

    private fun List<String>.hasPerfectColumnReflection(columnNumber: Int): Boolean {
        var lesser = columnNumber
        var greater = columnNumber + 1

        while (lesser >= 0 && greater <= this.first().length - 1) {
            if (this.map { it[lesser] } != this.map { it[greater] }) {
                return false
            }

            lesser--
            greater++
        }

        return true
    }

    private fun List<String>.hasAlmostPerfectRowReflection(rowNumber: Int): Boolean {
        var lesser = rowNumber
        var greater = rowNumber + 1

        var totalOffByOne = 0
        var totalUnequal = 0

        while (lesser >= 0 && greater <= this.size - 1) {
            // Get total number of differences between the two strings
            var totalDifferences = 0
            for (i in 0..<this[lesser].length) {
                if (this[lesser][i] != this[greater][i]) {
                    totalDifferences++
                }
            }
            if (totalDifferences == 1) {
                totalOffByOne++
            } else if (totalDifferences > 1) {
                totalUnequal++
            }

            lesser--
            greater++
        }

        return totalOffByOne == 1 && totalUnequal == 0
    }

    private fun List<String>.hasAlmostPerfectColumnReflection(columnNumber: Int): Boolean {
        var lesser = columnNumber
        var greater = columnNumber + 1

        var totalOffByOne = 0
        var totalUnequal = 0

        while (lesser >= 0 && greater <= this.first().length - 1) {
            // Get total number of differences between the two strings
            var totalDifferences = 0

            val left = this.map { it[lesser] }
            val right = this.map { it[greater] }

            for (i in left.indices) {
                if (left[i] != right[i]) {
                    totalDifferences++
                }
            }
            if (totalDifferences == 1) {
                totalOffByOne++
            } else if (totalDifferences > 1) {
                totalUnequal++
            }

            lesser--
            greater++
        }

        return totalOffByOne == 1 && totalUnequal == 0
    }
}