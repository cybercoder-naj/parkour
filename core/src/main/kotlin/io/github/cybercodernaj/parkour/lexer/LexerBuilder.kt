package io.github.cybercodernaj.parkour.lexer

import io.github.cybercodernaj.parkour.lexer.internal.Lexer

/**
 *
 * @author Nishant Aanjaney Jalan
 * @since 0.2.0
 */
class LexerBuilder internal constructor() {
  internal var ignorePattern: Regex = Regex("""\s+""")

  fun ignorePattern(regex: Regex) {
    ignorePattern = regex
  }
}

/**
 * Build a lexer in a DSL type language.
 * Accepts a function with [LexerBuilder] as a receiver and returns a lexer of that configuration.
 *
 * @param init the execution block in context of [LexerBuilder]
 * @return the configured Lexer to be used in the parser.
 *
 * @see [LexerBuilder]
 * @author Nishant Aanjaney Jalan
 * @since 0.2.0
 */
fun lexer(init: LexerBuilder.() -> Unit): Lexer {
  val builder = LexerBuilder()
  builder.init()
  return Lexer(
    ignorePattern = builder.ignorePattern
  )
}