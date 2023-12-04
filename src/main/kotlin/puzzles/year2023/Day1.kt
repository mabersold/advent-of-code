package puzzles.year2023

import puzzles.Puzzle

class Day1: Puzzle(2023, 1, "Trebuchet?!") {
    override fun part1(): Any {
        val input = getInput(1)
        return input.sumOf { findCalibrationValue(it) }
    }

    override fun part2(): Any {
        val input = getInput(2)
        return input.sumOf { findCalibrationValue(it, true) }
    }

    private fun findCalibrationValue(input: String, findSpelledDigits: Boolean = false): Int {
        val firstDigit = getFirstDigit(input, findSpelledDigits)
        val lastDigit = getLastDigit(input, findSpelledDigits)
        return "$firstDigit$lastDigit".toInt()
    }

    private fun getFirstDigit(input: String, findSpelledDigits: Boolean = false): Char {
        for (i in input.indices) {
            val spelledOutDigit = if (findSpelledDigits) spelledOutDigit(input.slice(i..<input.length)) else null

            if (spelledOutDigit != null) {
                return spelledOutDigit.digitToChar()
            }

            if (input[i].isDigit()) {
                return input[i]
            }
        }
        throw RuntimeException("No digits found in string")
    }

    private fun getLastDigit(input: String, findSpelledDigits: Boolean = false): Char {
        for (i in input.indices.reversed()) {
            val spelledOutDigit = if (findSpelledDigits) spelledOutDigit(input.slice(i..<input.length)) else null

            if (spelledOutDigit != null) {
                return spelledOutDigit.digitToChar()
            }

            if (input[i].isDigit()) {
                return input[i]
            }
        }
        throw RuntimeException("No digits found in string")
    }

    private fun spelledOutDigit(input: String): Int? {
        return when {
            input.startsWith("one") -> 1
            input.startsWith("two") -> 2
            input.startsWith("three") -> 3
            input.startsWith("four") -> 4
            input.startsWith("five") -> 5
            input.startsWith("six") -> 6
            input.startsWith("seven") -> 7
            input.startsWith("eight") -> 8
            input.startsWith("nine") -> 9
            else -> null
        }
    }
}