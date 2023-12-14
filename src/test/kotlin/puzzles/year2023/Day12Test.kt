package puzzles.year2023

import kotlin.test.Test
import kotlin.test.assertEquals

class Day12Test {
    @Test
    fun `part 1`() {
        assertEquals(21L, Day12().part1())
    }

    @Test
    fun `part 2`() {
        assertEquals(525152L, Day12().part2())
    }
}