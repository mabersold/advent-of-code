package puzzles.year2023

import puzzles.Puzzle

class Day12: Puzzle(2023, 12, "Hot Springs") {
    override fun part1(): Any {
        val input = getInput(1)

        return input.sumOf { line ->
            val diagram = line.split(" ").first()
            val arrangements = line.split(" ").last().split(",").map { it.toInt() }

            SpringSurprise(diagram, arrangements).totalArrangements()
        }
    }

    override fun part2(): Any {
        val input = getInput(1)

        return input.sumOf { line ->
            val diagram = line.split(" ").first()
            val arrangements = line.split(" ").last().split(",").map { it.toInt() }

            val fiveTimesDiagram = "$diagram?$diagram?$diagram?$diagram?$diagram"
            val fiveTimesArrangements = listOf(arrangements, arrangements, arrangements, arrangements, arrangements).flatten()

            SpringSurprise(fiveTimesDiagram, fiveTimesArrangements).totalArrangements()
        }
    }

    data class SpringSurprise(val diagram: String, val arrangements: List<Int>) {
        private val cache = hashMapOf<Triple<Int, Int, Int>, Long>()

        fun totalArrangements(position: Int = 0, currentGroup: Int = 0, groupLength: Int = 0): Long {
            if (cache.containsKey(Triple(position, currentGroup, groupLength))) {
                return cache[Triple(position, currentGroup, groupLength)]!!
            }

            if (position == diagram.length) {
                if (currentGroup == arrangements.size && groupLength == 0) {
                    cache[Triple(position, currentGroup, groupLength)] = 1
                    return 1
                } else if (currentGroup == arrangements.size - 1 && arrangements[currentGroup] == groupLength) {
                    cache[Triple(position, currentGroup, groupLength)] = 1
                    return 1
                }

                cache[Triple(position, currentGroup, groupLength)] = 0
                return 0
            }

            var count = 0L

            listOf('.', '#').forEach { c ->
                if (listOf(c, '?').contains(diagram[position])) {
                    if (c == '.') {
                        if (groupLength == 0) {
                            count += totalArrangements(position + 1, currentGroup, 0)
                        } else if (currentGroup < arrangements.size && groupLength == arrangements[currentGroup]) {
                            count += totalArrangements(position + 1, currentGroup + 1, 0)
                        }
                    } else {
                        count += totalArrangements(position + 1, currentGroup, groupLength + 1)
                    }
                }
            }

            cache[Triple(position, currentGroup, groupLength)] = count
            return count
        }
    }
}