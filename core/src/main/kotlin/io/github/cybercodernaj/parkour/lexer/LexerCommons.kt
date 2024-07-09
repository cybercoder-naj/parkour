package io.github.cybercodernaj.parkour.lexer

/**
 * A list of common patterns and lists of items that most programming languages and
 * data serialization formats.
 *
 * @author Nishant Aanjaney Jalan
 * @since 0.2.0
 */
object LexerCommons {
  val identifiers = Regex("""[a-zA-Z_]\w*""")
  val integerLiteral = Regex("""[-+]?[0-9_]+""")
  val floatingLiteral = Regex("""[-+]?[0-9_]*\.[0-9_]+(?:[eE][-+]?[0-9_]+)?""")
  val singleLineString: Set<String> = setOf("\"", "\'")
}
