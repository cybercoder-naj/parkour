package io.github.cybercodernaj.parkour.lexer.internal

import io.github.cybercodernaj.parkour.utils.Position

/**
 * A token is a single identifiable unit in a string.
 *
 * @author Nishant Aanjaney Jalan
 * @since 0.1.0
 */
internal sealed class Token(open val value: Any?, open val start: Position?, open val end: Position?) {
  class Identifier(override val value: String, start: Position, end: Position) : Token(value, start, end)

  class Keyword(override val value: String, start: Position, end: Position, val soft: Boolean = false) : Token(value, start, end)

  class Operator(override val value: String, start: Position, end: Position) : Token(value, start, end)

  class Separator(override val value: String, start: Position, end: Position) : Token(value, start, end)

  sealed class Literal(
    override val value: Any,
    override val start: Position,
    override val end: Position
  ) : Token(value, start, end) {
    class IntLiteral(override val value: Long, start: Position, end: Position) : Literal(value, start, end)
    class FloatLiteral(override val value: Double, start: Position, end: Position) : Literal(value, start, end)
    class StringLiteral(override val value: String, start: Position, end: Position) : Literal(value, start, end)
  }

  data object EOF : Token(null, null, null)

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Token) return false

    if (value != other.value) return false
    if (start != other.start) return false
    if (end != other.end) return false

    return true
  }

  override fun hashCode(): Int {
    var result = value?.hashCode() ?: 0
    result = 31 * result + (start?.hashCode() ?: 0)
    result = 31 * result + (end?.hashCode() ?: 0)
    return result
  }

  override fun toString(): String {
    return "${this::class.simpleName}(value=$value, start=$start, end=$end)"
  }
}