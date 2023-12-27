package puzzles.year2023

import kotlin.test.Test
import kotlin.test.assertEquals

class Day19Test {
    @Test
    fun `part 1`() {
        assertEquals(19114, Day19().part1())
    }

    @Test
    fun `part 2`() {
        assertEquals(167409079868000L, Day19().part2())
    }
}