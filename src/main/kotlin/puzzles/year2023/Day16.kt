package puzzles.year2023

import models.Direction
import puzzles.Puzzle

class Day16: Puzzle(2023, 16, "The Floor Will Be Lava") {
    override fun part1(): Any {
        val input = getInput(1)

        val energizedTiles = hashMapOf<Pair<Int, Int>, HashSet<Direction>>()

        beamMeUp(Pair(0, 0), Direction.RIGHT, input, energizedTiles)

        return energizedTiles.size
    }

    override fun part2(): Any {
        val input = getInput(1)

        var max = 0

        // top edge
        for (col in input.first().indices) {
            val energizedTiles = hashMapOf<Pair<Int, Int>, HashSet<Direction>>()
            beamMeUp(Pair(0, col), Direction.DOWN, input, energizedTiles)
            if (energizedTiles.size > max) {
                max = energizedTiles.size
            }
        }

        // bottom edge
        for (col in input.first().indices) {
            val energizedTiles = hashMapOf<Pair<Int, Int>, HashSet<Direction>>()
            beamMeUp(Pair(input.size - 1, col), Direction.UP, input, energizedTiles)
            if (energizedTiles.size > max) {
                max = energizedTiles.size
            }
        }

        // left edge
        for (row in input.indices) {
            val energizedTiles = hashMapOf<Pair<Int, Int>, HashSet<Direction>>()
            beamMeUp(Pair(row, 0), Direction.RIGHT, input, energizedTiles)
            if (energizedTiles.size > max) {
                max = energizedTiles.size
            }
        }

        // right edge
        for (row in input.indices) {
            val energizedTiles = hashMapOf<Pair<Int, Int>, HashSet<Direction>>()
            beamMeUp(Pair(row, input.first().length - 1), Direction.LEFT, input, energizedTiles)
            if (energizedTiles.size > max) {
                max = energizedTiles.size
            }
        }

        return max
    }

    private fun beamMeUp(currentTile: Pair<Int, Int>, direction: Direction, grid: List<String>, energizedTiles: HashMap<Pair<Int, Int>, HashSet<Direction>>) {
        // end condition 1: current tile is not a valid coordinate
        if (currentTile.first < 0 || currentTile.second < 0 || currentTile.first >= grid.size || currentTile.second >= grid.first().length) {
            return
        }

        // end condition 2: we are repeating ourselves
        // We know we are repeating ourselves if energizedTiles already contains the current pair and direction
        if (isRepeated(currentTile, direction, energizedTiles)) {
            return
        }

        addEnergizedTile(currentTile, direction, energizedTiles)

        when(grid[currentTile.first][currentTile.second]) {
            '.' -> beamMeUp(currentTile.getNext(direction), direction, grid, energizedTiles)
            '/' -> {
                when(direction) {
                    Direction.UP -> beamMeUp(currentTile.getNext(Direction.RIGHT), Direction.RIGHT, grid, energizedTiles)
                    Direction.DOWN -> beamMeUp(currentTile.getNext(Direction.LEFT), Direction.LEFT, grid, energizedTiles)
                    Direction.LEFT -> beamMeUp(currentTile.getNext(Direction.DOWN), Direction.DOWN, grid, energizedTiles)
                    Direction.RIGHT -> beamMeUp(currentTile.getNext(Direction.UP), Direction.UP, grid, energizedTiles)
                }
            }
            '\\' -> {
                when(direction) {
                    Direction.UP -> beamMeUp(currentTile.getNext(Direction.LEFT), Direction.LEFT, grid, energizedTiles)
                    Direction.DOWN -> beamMeUp(currentTile.getNext(Direction.RIGHT), Direction.RIGHT, grid, energizedTiles)
                    Direction.LEFT -> beamMeUp(currentTile.getNext(Direction.UP), Direction.UP, grid, energizedTiles)
                    Direction.RIGHT -> beamMeUp(currentTile.getNext(Direction.DOWN), Direction.DOWN, grid, energizedTiles)
                }
            }
            '|' -> {
                when(direction) {
                    Direction.DOWN, Direction.UP -> beamMeUp(currentTile.getNext(direction), direction, grid, energizedTiles)
                    Direction.LEFT, Direction.RIGHT -> {
                        beamMeUp(currentTile.getNext(Direction.UP), Direction.UP, grid, energizedTiles)
                        beamMeUp(currentTile.getNext(Direction.DOWN), Direction.DOWN, grid, energizedTiles)
                    }
                }
            }
            '-' -> {
                when(direction) {
                    Direction.LEFT, Direction.RIGHT -> beamMeUp(currentTile.getNext(direction), direction, grid, energizedTiles)
                    Direction.UP, Direction.DOWN -> {
                        beamMeUp(currentTile.getNext(Direction.LEFT), Direction.LEFT, grid, energizedTiles)
                        beamMeUp(currentTile.getNext(Direction.RIGHT), Direction.RIGHT, grid, energizedTiles)
                    }
                }
            }
        }
    }

    private fun isRepeated(currentTile: Pair<Int, Int>, direction: Direction, energizedTiles: HashMap<Pair<Int, Int>, HashSet<Direction>>): Boolean {
        return energizedTiles.containsKey(currentTile) && energizedTiles[currentTile]?.contains(direction) == true
    }

    private fun addEnergizedTile(currentTile: Pair<Int, Int>, direction: Direction, energizedTiles: HashMap<Pair<Int, Int>, HashSet<Direction>>) {
        if (energizedTiles.containsKey(currentTile)) {
            energizedTiles[currentTile]?.add(direction)
        } else {
            energizedTiles[currentTile] = hashSetOf(direction)
        }
    }

    private fun Pair<Int, Int>.getNext(direction: Direction): Pair<Int, Int> {
        return when (direction) {
            Direction.RIGHT -> Pair(this.first, this.second + 1)
            Direction.LEFT -> Pair(this.first, this.second - 1)
            Direction.UP -> Pair(this.first - 1, this.second)
            Direction.DOWN -> Pair(this.first + 1, this.second)
        }
    }
}