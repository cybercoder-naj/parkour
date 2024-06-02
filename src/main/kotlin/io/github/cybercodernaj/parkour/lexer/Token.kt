package io.github.cybercodernaj.parkour.lexer

import io.github.cybercodernaj.parkour.utils.Position

/**
 * A token is a single identifiable unit in a string.
 *
 * @author Nishant Aanjaney Jalan
 * @since 0.0.1
 */
internal sealed class Token {
  data class Identifier(val name: String, val start: Position, val end: Position) : Token()
  data class Keyword(val name: String, val start: Position, val end: Position, val soft: Boolean = false) : Token()
  data class Operator(val name: String, val start: Position, val end: Position) : Token()
  data class Separator(val name: String, val start: Position, val end: Position) : Token()
  data object EOF : Token()
}