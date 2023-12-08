package puzzles.year2023

import kotlin.test.Test
import kotlin.test.assertEquals

class Day2Test {
    @Test
    fun `part 1`() {
        val day2 = Day2()
        assertEquals(8, day2.part1())
    }

    @Test
    fun `part 2`() {
        val day2 = Day2()
        assertEquals(2286, day2.part2())
    }
}