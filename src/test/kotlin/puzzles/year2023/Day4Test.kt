package puzzles.year2023

import kotlin.test.Test
import kotlin.test.assertEquals

class Day4Test {
    private val day4 = Day4()

    @Test
    fun `part 1`() {
        assertEquals(13, day4.part1())
    }

    @Test
    fun `part 2`() {
        assertEquals(30, day4.part2())
    }
}