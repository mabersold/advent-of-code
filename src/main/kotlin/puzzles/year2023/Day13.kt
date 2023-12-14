package puzzles.year2023

import puzzles.Puzzle

class Day13: Puzzle(2023, 13, "Point of Incidence") {
    override fun part1(): Any {
        val input = getInputAsSingleString(1)

        val patterns = input.split("\n\n")

        return patterns.sumOf { p ->
            val pattern = p.split("\n")

            pattern.getAnswer()
        }
    }

    override fun part2(): Any {
        TODO("Not yet implemented")
    }

    private fun List<String>.findReflectedRows(): List<Int> {
        val reflectedRowNumbers = mutableListOf<Int>()

        for (c in 0..<this.size - 1) {
            if (this[c] == this[c+1]) {
                reflectedRowNumbers.add(c)
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

    private fun List<String>.getAnswer(): Int {
        val reflectedRows = this.findReflectedRows()
        val reflectedColumns = this.findReflectedColumns()

        reflectedRows.forEach { row ->
            if (this.hasPerfectRowReflection(row))
                return 100 * (row + 1)
        }

        reflectedColumns.forEach { col ->
            if (this.hasPerfectColumnReflection(col))
                return col + 1
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
}