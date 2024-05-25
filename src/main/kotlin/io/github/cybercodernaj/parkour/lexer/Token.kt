package io.github.cybercodernaj.parkour.lexer

import io.github.cybercodernaj.parkour.utils.Position

/**
 * A token is a single identifiable unit in a string.
 *
 * @author Nishant Aanjaney Jalan
 * @since 0.0.1
 */
internal sealed class Token(
  val start: Position?,
  val end: Position?
) {
  class Identifier(val name: String, start: Position, end: Position) : Token(start, end)
  data object EOF : Token(null, null)
}