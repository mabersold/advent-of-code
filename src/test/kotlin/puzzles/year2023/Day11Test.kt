package puzzles.year2023

import kotlin.test.Test
import kotlin.test.assertEquals

class Day11Test {
    @Test
    fun `part 1`() {
        assertEquals(374L, Day11().part1())
    }

    @Test
    fun `part 2`() {
        assertEquals(82000210L, Day11().part2())
    }
}