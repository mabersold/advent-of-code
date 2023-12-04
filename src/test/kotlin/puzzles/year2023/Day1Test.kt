package puzzles.year2023

import kotlin.test.Test
import kotlin.test.assertEquals

class Day1Test {
    @Test
    fun `part 1`() {
        val day1 = Day1()
        assertEquals(142, day1.part1())
    }

    @Test
    fun `part 2`() {
        val day1 = Day1()
        assertEquals(281, day1.part2())
    }
}