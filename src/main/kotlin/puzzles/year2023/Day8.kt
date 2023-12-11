package puzzles.year2023

import puzzles.Puzzle

class Day8: Puzzle(2023, 8, "Haunted Wasteland") {
    override fun part1(): Any {
        val input = getInput(1)
        val navigator = buildNavigator(input, Regex("^AAA$"))
        return navigator.getSolution(Regex("^ZZZ$"))
    }

    override fun part2(): Any {
        val input = getInput(2)
        val navigator = buildNavigator(input, Regex("^.+A$"))
        return navigator.getSolution(Regex("^.+Z$"))
    }

    private fun buildNavigator(input: List<String>, startPositionRegex: Regex): Navigator {
        val instructions = input[0]
        val lefts = hashMapOf<String, String>()
        val rights = hashMapOf<String, String>()
        val positions = arrayListOf<String>()

        input.subList(2, input.size).forEach { i ->
            val current = i.substring(0..2)
            val left = i.substring(7..9)
            val right = i.substring(12..14)

            lefts[current] = left
            rights[current] = right

            if (startPositionRegex.matches(current)) {
                positions.add(current)
            }
        }

        return Navigator(instructions, lefts, rights, positions)
    }

    data class Navigator(val instructions: String, val lefts: HashMap<String, String>, val rights: HashMap<String, String>, val startingPositions: List<String>) {
        fun getSolution(endCondition: Regex): Long {
            val cycleLengths = startingPositions.map { p ->
                getCycleLength(instructions, p, endCondition, lefts, rights).toLong()
            }

            return lcmFromList(cycleLengths)
        }

        private fun getCycleLength(instructions: String, startPosition: String, endCondition: Regex, lefts: HashMap<String, String>, rights: HashMap<String, String>): Int {
            var counter = 0
            var position = startPosition

            while(!endCondition.matches(position)) {
                val instruction = instructions[counter % instructions.length]

                position = if (instruction == 'R') {
                    rights[position] ?: throw RuntimeException("Right mapping not found")
                } else {
                    lefts[position] ?: throw RuntimeException("Left mapping not found")
                }

                counter++
            }

            return counter
        }

        private fun lcmFromList(numbers: List<Long>): Long {
            var result = numbers[0]
            for (i in 1..<numbers.size) {
                result = leastCommonMultiple(result, numbers[i])
            }
            return result
        }

        private fun leastCommonMultiple(first: Long, second: Long): Long {
            val larger = maxOf(first, second)
            val maxLcm = first * second
            var lcm = larger
            while (lcm < maxLcm) {
                if (lcm % first == 0L && lcm % second == 0L) {
                    return lcm
                }
                lcm += larger
            }
            return maxLcm
        }
    }
}