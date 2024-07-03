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

  internal var identifiers: Regex = Lexer.Defaults.identifiers
    private set

  private val _hardKeywords: MutableList<String> = mutableListOf()
  internal val hardKeywords: List<String> get() = _hardKeywords

  private val _separators: MutableList<String> = mutableListOf()
  internal val separators: List<String> get() = _separators

  private val _operators: MutableList<String> = mutableListOf()
  internal val operators: List<String> get() = _operators

  /**
   * The lexer will skip over any strings that match this regex.
   * This acts like a token separator.
   *
   * ### Usage
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
   * ### Usage
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
   * ### Usage
   *
   * ```kt
   * val myLexer = lexer {
   *   // You don't need the square brackets but KDoc doesn't like it...
   *   multilineComments(Regex("[/][*]") to Regex("[*][/]"))
   * }
   * ```
   *
   * @param multilineComments pair of regexes that define what contents are under comments.
   *
   * @see Lexer.Defaults.multilineComments
   * @author Nishant Aanjaney Jalan
   * @since 0.2.0
   */
  fun multilineComments(multilineComments: Pair<Regex, Regex>) {
    this.multilineComments = multilineComments
  }

  /**
   * Supply the regex pattern that defines the rules for identifiers.
   * Identifiers are parts of the program that are named by the user;
   * for instance, the field name or class name.
   *
   * ### Usage
   *
   * ```kt
   * val myLexer = lexer {
   *   // If all identifiers must be at least 2 characters and start with a lowercase english alphabet.
   *   identifiers(Regex("[a-z][a-zA-Z0-9]+"))
   * }
   * ```
   *
   * @param identifiers regex defining the rules of the naming identifiers
   *
   * @see Lexer.Defaults.identifiers
   * @author Nishant Aanjaney Jalan
   * @since 0.2.0
   */
  fun identifiers(identifiers: Regex) {
    this.identifiers = identifiers
  }

  /**
   * Hard keywords are a characters and symbols that give a particular meaning to a program.
   * They may not be used as identifiers.
   * (Default: [])
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
   * Separators are characters and symbols that act like delimiters to separate other meaningful elements. (Default: [])
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
   * Operators are characters and symbols that may perform arithmetic or logical operations. (Default: [])
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
  )
}