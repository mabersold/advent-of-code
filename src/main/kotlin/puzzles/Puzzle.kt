package puzzles

abstract class Puzzle(private val year: Int, private val day: Int, private val title: String) {
    abstract fun part1(): Any
    abstract fun part2(): Any

    fun displayResult() {
        println("--- $year Day $day: $title ---")
        println("Part 1 result: ${part1()}")
        println("Part 2 result: ${part2()}")
    }

    protected fun getInput(part: Int): List<String> {
        val input = this::class.java.getResource("/puzzle-input/$year/day$day-$part.txt")?.readText()
        return input?.split("\n") ?: emptyList()
    }

    protected fun getInputAsSingleString(part: Int): String {
        return this::class.java.getResource("/puzzle-input/$year/day$day-$part.txt")?.readText() ?: ""
    }
}