package puzzles.year2023

import java.util.PriorityQueue
import models.Direction
import models.Point
import puzzles.Puzzle

class Day17: Puzzle(2023, 17, "Clumsy Crucible") {
    override fun part1(): Any {
        val input = getInput(1)

        val grid = input.map { line ->
            line.map { c ->
                c.digitToInt()
            }.toIntArray()
        }.toTypedArray()

        val destination = Point(grid.size - 1, grid.first().size - 1)

        return getSolution(grid, destination, 0, 3)
    }

    override fun part2(): Any {
        // 1048 is too low
        val input = getInput(1)

        val grid = input.map { line ->
            line.map { c ->
                c.digitToInt()
            }.toIntArray()
        }.toTypedArray()

        val destination = Point(grid.size - 1, grid.first().size - 1)

        return getSolution(grid, destination, 4, 10)
    }

    private fun getSolution(grid: Array<IntArray>, destination: Point, minStraightSteps: Int, maxStraightSteps: Int): Int {
        val pq = PriorityQueue<Move>(compareBy { it.cost } )
        pq.add(Move(0, Point(0, 0), null, 0))
        val seen = hashSetOf<Triple<Point, Direction?, Int>>()

        while (pq.isNotEmpty()) {
            val move = pq.poll()

            if (move.currentPosition == destination && move.straightSteps >= minStraightSteps) {
                return move.cost
            }

            // continue if we've already seen the current configuration
            if (seen.contains(Triple(move.currentPosition, move.direction, move.straightSteps)))
                continue

            // add to seen
            seen.add(Triple(move.currentPosition, move.direction, move.straightSteps))

            if (move.direction == null) {
                pq.add(Move(move.cost + grid.costAtPoint(move.neighbor(Direction.DOWN)), move.neighbor(Direction.DOWN), Direction.DOWN, 1))
                pq.add(Move(move.cost + grid.costAtPoint(move.neighbor(Direction.RIGHT)), move.neighbor(Direction.RIGHT), Direction.RIGHT, 1))
            } else {
                // add clockwise, if in bounds and we are greater than minStraightSteps
                if (move.straightSteps >= minStraightSteps && move.neighbor(move.direction.clockwise()).isInBounds(grid.size - 1, grid.first().size - 1)) {
                    pq.add(
                        Move(
                            move.cost + grid.costAtPoint(move.neighbor(move.direction.clockwise())),
                            move.neighbor(move.direction.clockwise()),
                            move.direction.clockwise(),
                            1
                        )
                    )
                }

                // add counter-clockwise, if in bounds and we are greater than minStraightSteps
                if (move.straightSteps >= minStraightSteps && move.neighbor(move.direction.counterClockwise()).isInBounds(grid.size - 1, grid.first().size - 1)) {
                    pq.add(
                        Move(
                            move.cost + grid.costAtPoint(move.neighbor(move.direction.counterClockwise())),
                            move.neighbor(move.direction.counterClockwise()),
                            move.direction.counterClockwise(),
                            1
                        )
                    )
                }

                if (move.straightSteps < maxStraightSteps && move.neighbor(move.direction).isInBounds(grid.size - 1, grid.first().size - 1)) {
                    pq.add(
                        Move(
                            move.cost + grid.costAtPoint(move.neighbor(move.direction)),
                            move.neighbor(move.direction),
                            move.direction,
                            move.straightSteps + 1
                        )
                    )
                }
            }
        }

        return 0
    }

    data class Move(val cost: Int, val currentPosition: Point, val direction: Direction?, val straightSteps: Int) {
        fun neighbor(newDirection: Direction): Point = currentPosition.neighbor(newDirection)
    }

    private fun Array<IntArray>.costAtPoint(point: Point): Int {
        return this[point.row][point.column]
    }

    private fun Direction.clockwise(): Direction {
        return when (this) {
            Direction.UP -> Direction.RIGHT
            Direction.DOWN -> Direction.LEFT
            Direction.LEFT -> Direction.UP
            Direction.RIGHT -> Direction.DOWN
        }
    }

    private fun Direction.counterClockwise(): Direction {
        return when (this) {
            Direction.UP -> Direction.LEFT
            Direction.DOWN -> Direction.RIGHT
            Direction.LEFT -> Direction.DOWN
            Direction.RIGHT -> Direction.UP
        }
    }
}