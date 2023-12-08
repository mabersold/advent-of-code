package puzzles.year2023

import puzzles.Puzzle
import kotlin.math.max
import kotlin.math.min

class Day5: Puzzle(2023, 5, "If You Give A Seed A Fertilizer") {
    override fun part1(): Any {
        val input = getInput(1)

        val seeds = getSeeds(input)
        val mappers = getValueMappers(input)

        return seeds.minOf { seed ->
            mappers.transformValue(seed)
        }
    }

    override fun part2(): Any {
        val input = getInput(1)
        val seedRanges = getSeedRanges(input)

        val rangeMappers = getRangeMappers(input)


        var ranges = seedRanges
        for (rangeMapper in rangeMappers) {
            ranges = rangeMapper.transformRanges(ranges)
        }

        return ranges.minByOrNull { it.first }!!.first
    }

    private fun getSeeds(input: List<String>): List<Long> {
        val seedLine = input[0]
        return seedLine.removePrefix("seeds: ").split(" ").map { it.toLong() }
    }

    private fun getSeedRanges(input: List<String>): List<LongRange> {
        val seedLine = input[0]
        return seedLine.removePrefix("seeds: ").split(" ").windowed(2, 2).map {
            val startValue = it[0].toLong()
            val range = it[1].toLong()
            startValue..<startValue + range
        }
    }

    private fun getValueMappers(input: List<String>): List<List<ValueMapper>> {
        var collectionNumber = 0
        val mappers = arrayListOf(arrayListOf<ValueMapper>())

        for (i in 3..<input.size) {
            if (input[i] matches Regex("^\\d+\\s*(?:\\d+\\s*)*$")) {
                val lineValues = input[i].split(" ").map { it.toLong() }
                mappers[collectionNumber].add(ValueMapper(lineValues[1], lineValues[0], lineValues[2]))
            } else if (input[i] matches Regex("^.+\\smap:$")) {
                collectionNumber++
                mappers.add(arrayListOf())
            }
        }

        return mappers
    }

    private fun getRangeMappers(input: List<String>): List<RangeMapper> {
        val rangeMappers = input.joinToString("\n")
            .split("\n\n")
            .filterNot { it.startsWith("seeds") }
            .map { group -> group.split("\n").filterNot { it.endsWith(":") } }
            .map { group ->
                val sourceRanges = arrayListOf<LongRange>()
                val destinationRanges = arrayListOf<LongRange>()
                val sourceDestinationPairs = arrayListOf<Pair<LongRange, LongRange>>()
                for (lines in group) {
                    val splitValues = lines.split(" ").map { it.toLong() }
                    val sourceDestination = Pair(splitValues[1]..<splitValues[1] + splitValues[2], splitValues[0]..<splitValues[0] + splitValues[2])
                    sourceRanges.add(splitValues[1]..<splitValues[1] + splitValues[2])
                    destinationRanges.add(splitValues[0]..<splitValues[0] + splitValues[2])
                    sourceDestinationPairs.add(sourceDestination)
                }
                RangeMapper(sourceDestinationPairs)
            }

        return rangeMappers
    }

    private fun List<List<ValueMapper>>.transformValue(input: Long): Long {
        var result = input
        this.forEach { mappers ->
            result = mappers.getMappedValue(result)
        }
        return result
    }

    private fun List<ValueMapper>.getMappedValue(input: Long): Long {
        this.forEach { mapper ->
            mapper.getMappedValue(input)?.let { return it }
        }
        return input
    }

    data class ValueMapper(val startingPoint: Long, val mappingPoint: Long, val span: Long) {
        fun getMappedValue(input: Long): Long? {
            if (input in startingPoint..<startingPoint + span) {
                return (mappingPoint - startingPoint) + input
            }
            return null
        }
    }

    data class RangeMapper(
        val sourceDestinations: List<Pair<LongRange, LongRange>>
    ) {
        fun transformRanges(sources: List<LongRange>): List<LongRange> {
            val done = mutableListOf<LongRange>()
            val sourceRanges = mutableListOf<LongRange>()
            sourceRanges.addAll(sources)

            sourceDestinations.forEach { mapping ->
                val tempRanges = mutableListOf<LongRange>()

                for (range in sourceRanges) {
                    val left = range.first..min(range.last, mapping.first.first)
                    val middle = max(range.first, mapping.first.first)..min(mapping.first.last, range.last)
                    val right = max(mapping.first.last, range.first)..range.last

                    if (left.isAscending()) {
                        tempRanges.add(left)
                    }

                    if (middle.isAscending()) {
                        val shiftValue = mapping.second.first - mapping.first.first
                        val shiftedRange = middle.first + shiftValue..middle.last + shiftValue
                        done.add(shiftedRange)
                    }

                    if (right.isAscending()) {
                        tempRanges.add(right)
                    }
                }

                sourceRanges.clear()
                sourceRanges.addAll(tempRanges)
            }

            done.addAll(sourceRanges)
            return done
        }

        private fun LongRange.isAscending(): Boolean =
            this.first <= this.last
    }
}