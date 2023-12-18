package puzzles.year2023

import puzzles.Puzzle

class Day15: Puzzle(2023, 15, "Lens Library") {
    override fun part1(): Any {
        val input = getInputAsSingleString(1)

        return input.split(",").sumOf {
            hash(it)
        }
    }

    override fun part2(): Any {
        val input = getInputAsSingleString(1)

        val boxes = Array(256) { linkedMapOf<String, Int>() }

        input.split(",").forEach {
            if (it.last().isDigit()) {
                val focalLength = it.last().digitToInt()
                val label = it.split("=")[0]
                val boxNumber = hash(label)

                boxes[boxNumber][label] = focalLength
            } else {
                val label = it.split("-")[0]
                val boxNumber = hash(label)
                if (boxes[boxNumber].containsKey(label)) {
                    boxes[boxNumber].remove(label)
                }
            }
        }

        var totalFocusingPower = 0
        for (boxNumber in boxes.indices) {
            var slotNumber = 1
            boxes[boxNumber].forEach {
                totalFocusingPower += (boxNumber + 1) * slotNumber * it.value
                slotNumber++
            }
        }

        return totalFocusingPower
    }

    private fun hash(input: String): Int {
        var currentValue = 0

        input.forEach { c ->
            currentValue += c.code
            currentValue *= 17
            currentValue %= 256
        }

        return currentValue
    }
}