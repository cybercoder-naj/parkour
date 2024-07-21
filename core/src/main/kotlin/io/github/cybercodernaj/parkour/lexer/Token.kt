package io.github.cybercodernaj.parkour.lexer

import io.github.cybercodernaj.parkour.utils.Position

/**
 * A token is a single identifiable unit in a string.
 *
 * @author Nishant Aanjaney Jalan
 * @since 0.1.0
 */
internal sealed class Token(val value: Any, val start: Position, val end: Position) {
  class Identifier(value: String, start: Position, end: Position) : Token(value, start, end)
  class Keyword(value: String, start: Position, end: Position, val soft: Boolean = false) :
    Token(value, start, end)
  class Operator(value: String, start: Position, end: Position) : Token(value, start, end)
  class Separator(value: String, start: Position, end: Position) : Token(value, start, end)
  class IntLiteral(value: Long, start: Position, end: Position) : Token(value, start, end)
  class FloatLiteral(value: Double, start: Position, end: Position) : Token(value, start, end)
  class StringLiteral(value: String, start: Position, end: Position) : Token(value, start, end)
  class CharacterLiteral(value: Char, start: Position, end: Position) : Token(value, start, end)
  class BooleanLiteral(value: Boolean, start: Position, end: Position) : Token(value, start, end)

  data object EOF : Token(Any(), Position(-1, -1), Position(-1, -1))

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Token) return false

    if (value != other.value) return false
    if (start != other.start) return false
    if (end != other.end) return false

    return true
  }

  override fun hashCode(): Int {
    var result = value.hashCode()
    result = 31 * result + start.hashCode()
    result = 31 * result + end.hashCode()
    return result
  }

  override fun toString(): String {
    return "${this::class.simpleName}(value=$value, start=$start, end=$end)"
  }
}