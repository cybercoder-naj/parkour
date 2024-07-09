package io.github.cybercodernaj.parkour.lexer

import io.github.cybercodernaj.parkour.lexer.internal.Lexer

/**
 * A helper class to create the [Lexer].
 * Contains functions to be used as part of the [lexer] DSL.
 * Each property's default value is detailed in [LexerCommons].
 *
 * @author Nishant Aanjaney Jalan
 * @since 0.2.0
 */
class LexerBuilder internal constructor() {
  /**
   * The lexer will skip over any strings that match this regex.
   * This acts like a token separator.
   *
   * ### Usage
   *
   * ```kt
   * val myLexer = lexer {
   *   ignorePatterns = Regex("""\s+""")
   * }
   * ```
   *
   * @author Nishant Aanjaney Jalan
   * @since 0.2.0
   */
  var ignorePattern: Regex? = null

  /**
   * When the lexer identifies a [singleLineComments] pattern, it will skip to the next line
   * and return the next token.
   *
   * ### Usage
   *
   * ```kt
   * val myLexer = lexer {
   *   singleLineComments = Regex("//")
   * }
   * ```
   *
   * @author Nishant Aanjaney Jalan
   * @since 0.2.0
   */
  var singleLineComments: Regex? = null

  /**
   * There are two parts to [multilineComments]: the starting and the ending pattern.
   * When the lexer identifies the starting pattern, it will continue to skip to the next possible token
   * until it meets the ending pattern.
   *
   * ### Usage
   *
   * ```kt
   * val myLexer = lexer {
   *   // You don't need the square brackets but KDoc doesn't like it...
   *   multilineComments = Regex("[/][*]") to Regex("[*][/]")
   * }
   * ```
   *
   * @author Nishant Aanjaney Jalan
   * @since 0.2.0
   */
  var multilineComments: Pair<Regex, Regex>? = null

  /**
   * The regex pattern that defines the rules for identifiers.
   * Identifiers are parts of the program that are named by the user;
   * for instance, the field name or class name.
   *
   * ### Usage
   *
   * ```kt
   * val myLexer = lexer {
   *   // If all identifiers must be at least 2 characters and start with a lowercase english alphabet.
   *   identifiers = Regex("[a-z][a-zA-Z0-9]+")
   * }
   * ```
   *
   * @see LexerCommons.identifiers
   * @author Nishant Aanjaney Jalan
   * @since 0.2.0
   */
  var identifiers: Regex? = null

  private val _hardKeywords: MutableList<String> = mutableListOf()
  internal val hardKeywords: List<String> get() = _hardKeywords

  private val _separators: MutableList<String> = mutableListOf()
  internal val separators: List<String> get() = _separators

  private val _operators: MutableList<String> = mutableListOf()
  internal val operators: List<String> get() = _operators

  /**
   * The regex that detects and extracts integer literals from the string.
   * This should usually only consider strict integers.
   * This configuration is required only if your integer pattern is different from [LexerCommons.integerLiteral]
   *
   * ### Usage
   *
   * ```kt
   * val myLexer = lexer {
   *   integerLiteral = Regex("[+-]?[0-9_]+")
   * }
   * ```
   *
   * @see LexerCommons.integerLiteral
   * @author Nishant Aanjaney Jalan
   * @since 0.2.0
   */
  var integerLiteral: Regex = LexerCommons.integerLiteral

  /**
   * The regex that detects and extracts floating point literals from the string.
   * This should usually only consider numbers that have a decimal point.
   * This configuration is required only if your floating pattern is different from [LexerCommons.floatingLiteral]
   *
   * ### Usage
   *
   * ```kt
   * val myLexer = lexer {
   *   floatingLiteral = Regex("[-+]?[0-9_]*\.[0-9_]+(?:[eE][-+]?[0-9_]+)?")
   * }
   * ```
   *
   * @see LexerCommons.floatingLiteral
   * @author Nishant Aanjaney Jalan
   * @since 0.2.0
   */
  var floatingLiteral: Regex = LexerCommons.floatingLiteral

  /**
   * The enclosing strings that should denote the start and end of single-line strings.
   * The lexer will throw a [LexicalException] when a string literal is not terminated in the same line.
   * This configuration is required only if your string defining patterns are different from [LexerCommons.floatingLiteral]
   *
   * ### Usage
   *
   * ```kt
   * val myLexer = lexer {
   *   singleLineString = setOf("'", "\"")
   * }
   * ```
   *
   * @see LexerCommons.singleLineString
   * @author Nishant Aanjaney Jalan
   * @since 0.2.0
   */
  var singleLineString: Set<String> = LexerCommons.singleLineString

  /**
   * Hard keywords are a characters and symbols that give a particular meaning to a program.
   * They may not be used as identifiers.
   * You can add multiple keywords over many function calls.
   *
   * ### Usage
   *
   * ```kt
   * val myLexer = lexer {
   *   hardKeywords("class", "this", "fun", "keywords")
   * }
   * ```
   *
   * @param keywords a variable number of hard keywords
   * @author Nishant Aanjaney Jalan
   * @since 0.2.0
   */
  fun hardKeywords(vararg keywords: String) {
    this._hardKeywords.addAll(keywords)
  }

  /**
   * Separators are characters and symbols that act like delimiters to separate other meaningful elements.
   * You can add multiple separators over many function calls.
   *
   * ### Usage
   *
   * ```kt
   * val myLexer = lexer {
   *   separators(":", ",", "(", ")")
   * }
   * ```
   *
   * @param separators a variable number of separators
   * @author Nishant Aanjaney Jalan
   * @since 0.2.0
   */
  fun separators(vararg separators: String) {
    this._separators.addAll(separators)
  }

  /**
   * Operators are characters and symbols that may perform arithmetic or logical operations.
   * You can add multiple separators over many function calls.
   *
   * ### Usage
   *
   * ```kt
   * val myLexer = lexer {
   *   operators("+", "-", "||", "<=")
   * }
   * ```
   *
   * @param operators a variable number of operators
   * @author Nishant Aanjaney Jalan
   * @since 0.2.0
   */
  fun operators(vararg operators: String) {
    this._operators.addAll(operators)
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
    multilineComments = builder.multilineComments,
    identifiers = builder.identifiers,
    hardKeywords = builder.hardKeywords.sortedByDescending(String::length),
    operators = builder.operators.sortedByDescending(String::length),
    separators = builder.separators.sortedByDescending(String::length),
    integerLiteral = builder.integerLiteral,
    floatingLiteral = builder.floatingLiteral,
    singleLineString = builder.singleLineString,
  )
}