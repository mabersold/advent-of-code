package puzzles.year2023

import kotlin.test.Test
import kotlin.test.assertEquals

class Day5Test {
    private val day5 = Day5()

    @Test
    fun `part 1`() {
        assertEquals(35L, day5.part1())
    }

    @Test
    fun `part 2`() {
        assertEquals(46L, day5.part2())
    }
}