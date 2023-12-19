package puzzles.year2023

import models.Direction
import puzzles.Puzzle

class Day10: Puzzle(2023, 10, "Pipe Maze") {
    override fun part1(): Any {
        val input = getInput(1)

        // 1. Determine our starting point
        val startingCoordinates = input.getStartingCoordinates()
        val startingPoint = Tile('S', 0, startingCoordinates)

        // 2. Create a set to know which parts of the grid we have already visited, and populate our queue with the starting point
        val visitedCoordinates = mutableSetOf<Pair<Int, Int>>()
        val queue = arrayListOf(startingPoint)

        // 3. Process the queue and return the maximum value
        return processQueue(queue, visitedCoordinates, input)
    }

    override fun part2(): Any {
        val input = getInput(1)

        // 1. Determine our starting point
        val startingCoordinates = input.getStartingCoordinates()
        val startingPoint = Tile('S', 0, startingCoordinates)

        // 2. Create a set to know which parts of the grid we have already visited, and populate our queue with the starting point
        val visitedCoordinates = mutableSetOf<Pair<Int, Int>>()
        val queue = arrayListOf(startingPoint)

        // 3. Process the queue (we can ignore the return value)
        processQueue(queue, visitedCoordinates, input)

        var isInside = false
        var totalInterior = 0

        /**
         * We will need to iterate through the entire input to determine which cells are contained within the loop.
         * ProcessQueue, which we called earlier, populates visitedCoordinates with all the cells that are part of the
         * loop. Now that we know this information, we can use it to identify the interior cells that we need to count.
         */
        for (row in input.indices) {
            for (column in input[row].indices) {
                // Special case if the tile is S: convert it to whatever pipe it behaves as.
                val tileType = if (input[row][column] == 'S') {
                    input.convertStartingPoint(Pair(row, column))
                } else {
                    input[row][column]
                }

                if (listOf('|', 'L', 'J').contains(tileType) && visitedCoordinates.contains(Pair(row, column))) {
                    /**
                     * If the tile type has a vertical element, and the tile is part of the loop, we need to flip our
                     * isInside bit here. If it's true, we know we are in the interior of the loop.
                     */
                    isInside = !isInside
                } else if(isInside && !visitedCoordinates.contains(Pair(row, column))) {
                    /**
                     * If we are inside the loop, and the current tile is not itself a part of the loop, increment the
                     * counter. We know which tiles are part of the loop because they are stored in visitedCoordinates.
                     */
                    totalInterior++
                }
            }
            // At the end of each row, this needs to be set to false, because by definition we are outside of the loop
            // at the start of a new row
            isInside = false
        }

        return totalInterior
    }

    private fun processQueue(queue: ArrayList<Tile>, visitedCoordinates: MutableSet<Pair<Int, Int>>, input: List<String>): Int {
        var maxDistance = 0

        while (queue.isNotEmpty()) {
            val nextTile = queue.first()
            queue.removeAt(0)

            // If we have already visited this tile, we have no need to continue processing it.
            if (visitedCoordinates.contains(nextTile.coordinates))
                continue

            // Add tile coordinates to the visited set, so we know not to process it again
            visitedCoordinates.add(nextTile.coordinates)
            if (nextTile.stepsFromStart > maxDistance)
                maxDistance = nextTile.stepsFromStart

            // Retrieve the adjacent cells that are part of the loop
            val neighborCoordinates = input.getValidAdjacentCells(nextTile.coordinates)

            // If an adjacent cell has not been visited yet, create a tile object for it and add it to the queue
            // stepsFromStart must be one higher than the current tile
            neighborCoordinates.forEach {  neighbor ->
                if (!visitedCoordinates.contains(neighbor)) {
                    val newTile = Tile(
                        input[neighbor.first][neighbor.second],
                        nextTile.stepsFromStart + 1,
                        neighbor
                    )
                    queue.add(newTile)
                }
            }
        }

        return maxDistance
    }

    /**
     * Special function for part 2: We need to know how the S tile behaves, so we convert it into the tile part that
     * it behaves as. This is needed to know when we flip the isInside bit during our part 2 algorithm.
     */
    private fun List<String>.convertStartingPoint(coordinates: Pair<Int, Int>): Char {
        if (this[coordinates.first][coordinates.second] == 'S') {
            val validAdjacentCells = this.getValidAdjacentCells(coordinates)

            val hasTop = validAdjacentCells.contains(Pair(coordinates.first - 1, coordinates.second))
            val hasBottom = validAdjacentCells.contains(Pair(coordinates.first + 1, coordinates.second))
            val hasLeft = validAdjacentCells.contains(Pair(coordinates.first, coordinates.second - 1))
            val hasRight = validAdjacentCells.contains(Pair(coordinates.first, coordinates.second + 1))

            return when {
                hasTop && hasBottom -> '|'
                hasTop && hasRight -> 'L'
                hasTop && hasLeft -> 'J'
                hasLeft && hasRight -> '-'
                hasBottom && hasRight -> 'F'
                hasBottom && hasLeft -> '7'
                else -> 'S'
            }
        }

        return this[coordinates.first][coordinates.second]
    }

    private fun List<String>.getStartingCoordinates(): Pair<Int, Int> {
        for (row in this.indices) {
            for (column in this[row].indices) {
                if (this[row][column] == 'S')
                    return Pair(row, column)
            }
        }
        throw RuntimeException("Starting point not found")
    }

    /**
     * For a set of coordinates, return all coordinates of adjacent tiles that can be part of the loop. This function
     * does not consider whether a set of coordinates has been visited already.
     */
    private fun List<String>.getValidAdjacentCells(coordinates: Pair<Int, Int>): List<Pair<Int, Int>> {
        val currentTile = this[coordinates.first][coordinates.second]

        val maxRow = this.size - 1
        val maxColumn = this.first().length - 1

        // Based on the current tile's type, we can determine which directions we can possibly go. All tiles except
        // S can go in two directions, but the directions for an S tile are not known until we see what its neighbors are
        val validDirections = when(currentTile) {
            'S' -> listOf(Direction.DOWN, Direction.UP, Direction.LEFT, Direction.RIGHT)
            '|' -> listOf(Direction.DOWN, Direction.UP)
            '-' -> listOf(Direction.RIGHT, Direction.LEFT)
            'L' -> listOf(Direction.UP, Direction.RIGHT)
            'J' -> listOf(Direction.UP, Direction.LEFT)
            '7' -> listOf(Direction.DOWN, Direction.LEFT)
            'F' -> listOf(Direction.DOWN, Direction.RIGHT)
            else -> listOf()
        }

        val returnList = arrayListOf<Pair<Int, Int>>()

        /**
         * Given the possible directions, create a list of all valid adjacent coordinates that continue the loop.
         * This accounts for array boundaries, although maybe it doesn't need to. We use the permittedTravel map to
         * know whether it's legal to go from tile of type A to tile of type B in the specified direction. If true,
         * add the coordinate pair to the return list.
         */
        validDirections.forEach { direction ->
            if (direction == Direction.RIGHT && coordinates.second < maxColumn) {
                val rightNeighbor = this[coordinates.first][coordinates.second + 1]
                if(permittedTravel[Pair(currentTile,rightNeighbor)]?.contains(direction) == true) {
                    returnList.add(Pair(coordinates.first, coordinates.second + 1))
                }
            }
            if (direction == Direction.LEFT && coordinates.second > 0) {
                val leftNeighbor = this[coordinates.first][coordinates.second - 1]
                if (permittedTravel[Pair(currentTile, leftNeighbor)]?.contains(direction) == true) {
                    returnList.add(Pair(coordinates.first, coordinates.second - 1))
                }
            }
            if (direction == Direction.DOWN && coordinates.first < maxRow) {
                val downNeighbor = this[coordinates.first + 1][coordinates.second]
                if (permittedTravel[Pair(currentTile, downNeighbor)]?.contains(direction) == true) {
                    returnList.add(Pair(coordinates.first + 1, coordinates.second))
                }
            }
            if (direction == Direction.UP && coordinates.first > 0) {
                val upNeighbor = this[coordinates.first - 1][coordinates.second]
                if (permittedTravel[Pair(currentTile, upNeighbor)]?.contains(direction) == true) {
                    returnList.add(Pair(coordinates.first - 1, coordinates.second))
                }
            }
        }

        return returnList
    }

    companion object {
        /**
         * This map determines what are the legal directions when going from a tile of type 1 to a tile of type 2.
         * Because these definitions never change, it's okay to make this immutable.
         */
        private val permittedTravel = mapOf(
            Pair('S', '|') to listOf(Direction.UP, Direction.DOWN),
            Pair('S', '-') to listOf(Direction.LEFT, Direction.RIGHT),
            Pair('S', 'L') to listOf(Direction.DOWN, Direction.LEFT),
            Pair('S', 'J') to listOf(Direction.DOWN, Direction.RIGHT),
            Pair('S', '7') to listOf(Direction.UP, Direction.RIGHT),
            Pair('S', 'F') to listOf(Direction.UP, Direction.LEFT),
            Pair('|', '|') to listOf(Direction.DOWN, Direction.UP),
            Pair('|', 'L') to listOf(Direction.DOWN),
            Pair('|', 'J') to listOf(Direction.DOWN),
            Pair('|', '7') to listOf(Direction.UP),
            Pair('|', 'F') to listOf(Direction.UP),
            Pair('|', 'S') to listOf(Direction.DOWN, Direction.UP),
            Pair('-', '-') to listOf(Direction.LEFT, Direction.RIGHT),
            Pair('-', 'L') to listOf(Direction.LEFT),
            Pair('-', 'J') to listOf(Direction.RIGHT),
            Pair('-', '7') to listOf(Direction.RIGHT),
            Pair('-', 'F') to listOf(Direction.LEFT),
            Pair('-', 'S') to listOf(Direction.LEFT, Direction.RIGHT),
            Pair('L', '|') to listOf(Direction.UP),
            Pair('L', '-') to listOf(Direction.RIGHT),
            Pair('L', 'J') to listOf(Direction.RIGHT),
            Pair('L', '7') to listOf(Direction.RIGHT, Direction.UP),
            Pair('L', 'F') to listOf(Direction.UP),
            Pair('L', 'S') to listOf(Direction.UP, Direction.RIGHT),
            Pair('J', '|') to listOf(Direction.UP),
            Pair('J', '-') to listOf(Direction.LEFT),
            Pair('J', 'L') to listOf(Direction.LEFT),
            Pair('J', '7') to listOf(Direction.UP),
            Pair('J', 'F') to listOf(Direction.UP, Direction.LEFT),
            Pair('J', 'S') to listOf(Direction.UP, Direction.LEFT),
            Pair('7', '|') to listOf(Direction.DOWN),
            Pair('7', '-') to listOf(Direction.LEFT),
            Pair('7', 'J') to listOf(Direction.DOWN),
            Pair('7', 'L') to listOf(Direction.DOWN, Direction.LEFT),
            Pair('7', 'F') to listOf(Direction.LEFT),
            Pair('7', 'S') to listOf(Direction.DOWN, Direction.LEFT),
            Pair('F', '|') to listOf(Direction.DOWN),
            Pair('F', '-') to listOf(Direction.RIGHT),
            Pair('F', 'J') to listOf(Direction.RIGHT, Direction.DOWN),
            Pair('F', '7') to listOf(Direction.RIGHT),
            Pair('F', 'L') to listOf(Direction.DOWN),
            Pair('F', 'S') to listOf(Direction.DOWN, Direction.RIGHT),
        )
    }

    data class Tile(
        val type: Char,
        val stepsFromStart: Int,
        val coordinates: Pair<Int, Int>
    )
}