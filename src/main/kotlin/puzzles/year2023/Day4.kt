package puzzles.year2023

import puzzles.Puzzle
import kotlin.math.pow

class Day4: Puzzle(2023, 4, "Scratchcards") {
    override fun part1(): Any {
        val input = getInput(1)

        return input.sumOf { line ->
            2.0F.pow(line.getMatchingNumbers() - 1).toInt()
        }
    }

    override fun part2(): Any {
        val input = getInput(1)
        val totalCards = input.size
        val cardCopies = Array(totalCards) { 1 }

        input.forEachIndexed { i, line ->
            val matchingNumbers = line.getMatchingNumbers()
            val toAdd = cardCopies[i]
            if (matchingNumbers > 0) {
                for (j in getRange(i, matchingNumbers, totalCards - 1)) {
                    cardCopies[j] += toAdd
                }
            }
        }

        return cardCopies.sum()
    }

    private fun getRange(i: Int, matchingNumbers: Int, max: Int) =
        (i + 1).coerceAtMost(max)..(i + matchingNumbers).coerceAtMost(max)

    private fun String.getMatchingNumbers(): Int {
        val colonIndex = this.indexOf(":")
        val cardLines = this.substring(colonIndex + 2).split("|").map { it.trim() }

        val winningCards = cardLines.first().split(" ").filter { it.isNotBlank() }
        val myCards = cardLines.last().split(" ").filter { it.isNotBlank() }

        return winningCards.toSet().intersect(myCards.toSet()).size
    }
}