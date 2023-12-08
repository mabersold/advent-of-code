package puzzles.year2023

import puzzles.Puzzle

class Day2: Puzzle(2023, 2, "Cube Conundrum") {
    override fun part1(): Any {
        val input = getInput(1)
        return input.sumOf { isGamePossible(it) }
    }

    override fun part2(): Any {
        val input = getInput(1)
        return input.sumOf { gamePowerSet(it) }
    }

    private fun isGamePossible(gameData: String): Int {
        if (gameData.splitIntoRounds().all { isRoundPossible(it) }) {
            return getGameId(gameData)
        }
        return 0
    }

    private fun gamePowerSet(gameData: String): Int {
        var maxRed = 0
        var maxBlue = 0
        var maxGreen = 0

        gameData.splitIntoRounds().forEach { roundData ->
            val round = getRound(roundData)
            round["blue"]?.let { v -> if (v > maxBlue) maxBlue = v }
            round["green"]?.let { v -> if (v > maxGreen) maxGreen = v }
            round["red"]?.let { v -> if (v > maxRed) maxRed = v }
        }

        return maxRed * maxBlue * maxGreen
    }

    private fun isRoundPossible(roundData: String): Boolean {
        val round = getRound(roundData)

        return !((round["blue"] ?: 0) > 14 || (round["green"] ?: 0) > 13 || (round["red"] ?: 0) > 12)
    }

    private fun getGameId(gameData: String): Int {
        return gameData.substring(5..<gameData.indexOf(":")).toInt()
    }

    private fun String.splitIntoRounds() =
        this.slice(startingPoint(this)..<this.length).split("; ")

    private fun startingPoint(gameData: String): Int =
        gameData.indexOf(": ") + 2

    private fun getRound(roundData: String): Map<String, Int> {
        val splitRound = roundData.split(", ")
        val roundMap = mutableMapOf<String, Int>()
        splitRound.forEach { roundDatum ->
            val data = roundDatum.split(" ")
            roundMap[data.last()] = data.first().toInt()
        }
        return roundMap
    }
}