package puzzles.year2023

import kotlin.test.Test
import kotlin.test.assertEquals

class Day3Test {
    private val day3 = Day3()

    @Test
    fun `part 1`() {
        assertEquals(4361, day3.part1())
    }

    @Test
    fun `part 2`() {
        assertEquals(467835, day3.part2())
    }
}