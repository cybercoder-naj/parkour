package io.github.cybercodernaj.parkour.lexer

import io.github.cybercodernaj.parkour.lexer.internal.Lexer

/**
 * A helper class to create the [Lexer].
 * Contains functions to be used as part of the [lexer] DSL.
 * Each property's default value is detailed in [Lexer.Defaults].
 *
 * @author Nishant Aanjaney Jalan
 * @since 0.2.0
 */
class LexerBuilder internal constructor() {
  internal var ignorePattern: Regex = Lexer.Defaults.ignorePattern
    private set

  internal var singleLineComments: Regex? = Lexer.Defaults.singleLineComments
    private set

  internal var multilineComments: Pair<Regex, Regex>? = Lexer.Defaults.multilineComments
    private set

  /**
   * The lexer will skip over any strings that match this regex.
   * This acts like a token separator.
   *
   * ```kt
   * val myLexer = lexer {
   *   ignorePatterns(Regex("""\s+"""))
   * }
   * ```
   *
   * @param regex regex of the pattern the lexer will not tokenize.
   *
   * @see Lexer.Defaults.ignorePattern
   * @author Nishant Aanjaney Jalan
   * @since 0.2.0
   */
  fun ignorePattern(regex: Regex) {
    ignorePattern = regex
  }

  /**
   * When the lexer identifies a [singleLineComments] pattern, it will skip to the next line
   * and return the next token.
   *
   * ```kt
   * val myLexer = lexer {
   *   singleLineComments(Regex("//"))
   * }
   * ```
   *
   * @param singleLineComments regex of the pattern the lexer will skip over to the next line.
   *
   * @see Lexer.Defaults.singleLineComments
   * @author Nishant Aanjaney Jalan
   * @since 0.2.0
   */
  fun singleLineComments(singleLineComments: Regex) {
    this.singleLineComments = singleLineComments
  }

  /**
   * There are two parts to [multilineComments]: the starting and the ending pattern.
   * When the lexer identifies the starting pattern, it will continue to skip to the next possible token
   * until it meets the ending pattern.
   *
   * ```kt
   * val myLexer = lexer {
   *   // You don't need the square brackets but KDoc doesn't like it...
   *   multiLineComments(Regex("[/][*]") to Regex("[*][/]"))
   * }
   * ```
   *
   * @param singleLineComments regex of the pattern the lexer will skip over to the next line.
   *
   * @see Lexer.Defaults.singleLineComments
   * @author Nishant Aanjaney Jalan
   * @since 0.2.0
   */
  fun multiLineComments(multilineComments: Pair<Regex, Regex>) {
    this.multilineComments = multilineComments
  }
}

/**
 * Build a lexer in a DSL.
 * Accepts a function with [LexerBuilder] as a receiver and returns a lexer of that configuration.
 *
 * ### Sample
 *
 * ```kotlin
 * val myLexer = lexer {
 *   /* Add your lexer configuration here. */
 * }
 * ```
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
    ignorePattern = builder.ignorePattern,
    singleLineComments = builder.singleLineComments,
    multilineComments = builder.multilineComments
  )
}