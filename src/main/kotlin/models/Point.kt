package models

data class Point(val row: Int, val column: Int) {
    fun neighbor(direction: Direction): Point {
        return when (direction) {
            Direction.UP -> Point(row - 1, column)
            Direction.DOWN -> Point(row + 1, column)
            Direction.LEFT -> Point(row, column - 1)
            Direction.RIGHT -> Point(row, column + 1)
        }
    }

    fun isInBounds(maxRow: Int, maxColumn: Int, minRow: Int = 0, minColumn: Int = 0): Boolean {
        return row in minRow..maxRow && column in minColumn..maxColumn
    }

    operator fun plus(other: Point): Point =
        Point(row + other.row, column + other.column)

    operator fun times(amount: Int): Point =
        Point(row * amount, column * amount)
}