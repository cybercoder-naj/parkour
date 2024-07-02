package io.github.cybercodernaj.parkour.utils

/**
 * Similar to a [Pair] class of two integers.
 * Holds the line and column number in the contents defined by the strategies.
 *
 * @author Nishant Aanjaney Jalan
 * @since 0.1.0
 */
internal data class Position(
  val line: Int,
  val col: Int
) {
  fun nextLine(): Position {
    return Position(line + 1, 0)
  }

//  operator fun plus(other: Position): Position {
//    return Position(this.line + other.line, this.col + other.col)
//  }

  operator fun plus(colOffset: Int): Position =
    Position(line, col + colOffset)

  operator fun inc(): Position =
    plus(1)

  operator fun rangeTo(end: Position): IntRange {
    return this.col..end.col
  }

  operator fun compareTo(end: Position): Int {
    return this.col compareTo end.col
  }
}

internal operator fun Position.plus(other: Position): Position {
  return Position(this.line + other.line, this.col + other.col)
}
