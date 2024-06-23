package io.github.cybercodernaj.parkour.lexer

/**
 *
 * @author Nishant Aanjaney Jalan
 * @since 0.1.0
 */
class Literals(
  internal val integerLiteral: Regex? = Regex("""[-+]?[0-9_]+"""),
  internal val floatingLiteral: Regex? = Regex("""[-+]?[0-9_]*\.[0-9_]+(?:[eE][-+]?[0-9_]+)?"""),
  internal val stringEnclosure: String = "\"",
  internal val booleanLiteral: Regex? = Regex("true|false"),
  internal val escapeSequences: Map<Regex, Int> = mapOf(),
)