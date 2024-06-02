package io.github.cybercodernaj.parkour.lexer

import io.github.cybercodernaj.parkour.datasource.TextSource
import io.github.cybercodernaj.parkour.exceptions.LexicalException
import io.github.cybercodernaj.parkour.utils.Position

/**
 * The lexer is responsible to convert the given string into a stream of [Token]s.
 * The lexer take in multiple settings that configure how it behaves.
 * It will perform lexical analysis on a line-by-line basis and return the next unconsumed token.
 * A newline character is **always** separates a token.
 *
 * @constructor Creates a lexer with the provided properties.
 * @param ignorePattern characters that satisfy this regex would be skipped. (Default: "\s+")
 * @param singleLineComments The regex that defines how a single-line comment starts.
 *    Once identified, the lexer will skip the remaining line. (Default: null)
 * @param multilineComments A pair of regexes, the starting pattern and the ending pattern for a
 *    multiline comment block. (Default: null)
 * @param identifiers A regex string that defines the rules for defining a name. (Default: "[a-zA-Z_]\w*")
 * @param hardKeywords A list of strings that are considered hard keywords.
 *    Hard keywords are a characters and symbols that give a particular meaning to a program. (Default: [])
 * @param operators A regex that are considered as operators.
 *    Operators are characters and symbols that may perform arithmetic or logical operations.
 *    Longer operators must be earlier in the pattern to be matched. (Default: [])
 * @param separators A regex that are considered as separators.
 *    Separators are characters and symbols that act like delimiters to separate other meaningful elements.
 *    Longer separators must be earlier in the pattern to be matched. (Default: [])
 * @param literals The configuration of literals. Literals denote constant values
 * such as numbers, strings, and characters. (Default: see [Literals])
 *
 * @author Nishant Aanjaney Jalan
 * @since 0.0.1
 */
class Lexer(
  private val ignorePattern: Regex = Regex("""\s+"""),
  private val singleLineComments: Regex? = null,
  private val multilineComments: Pair<Regex, Regex>? = null,
  private val identifiers: Regex = Regex("""[a-zA-Z_]\w*"""),
  private val hardKeywords: List<String> = emptyList(),
  private val operators: Regex? = null,
  private val separators: Regex? = null,
  private val literals: Literals = Literals()
) {
  private var position = Position(0, 0)

  internal lateinit var source: TextSource

  private var _currentLine: String? = null
  private val currentLine: String
    get() = _currentLine!!

  private var tokenIndex = 0
  private var tokenStream = emptyList<Token>()

  private val combinedTokenSeparator: Regex

  private var insideMultilineComment = false

  init {
    combinedTokenSeparator = buildDisjunctRegex()
  }

  /**
   * Fetches the next [Token] from the source
   *
   * @author Nishant Aanjaney Jalan
   * @since 0.0.1
   */
  internal fun nextToken(): Token {
    if (tokenIndex >= tokenStream.size)
      updateTokenStream()

    if (tokenStream.isEmpty()) {
      adjustPositionIfNeeded()
      return nextToken()
    }

    return tokenStream[tokenIndex++]
  }

  private fun updateTokenStream() {
    fetchNextLine()
    tokenIndex = 0 // reset the counter

    if (_currentLine == null) {
      tokenStream = listOf(Token.EOF)
      return
    }

    val tokenStream = mutableListOf<Token>()
    while (position.col < currentLine.length) {
      if (position isAt singleLineComments) {
        position = position.copy(col = currentLine.length)
        break
      }

      if (!insideMultilineComment && position isAt multilineComments?.first) {
        insideMultilineComment = true
      }
      if (insideMultilineComment) {
        val match = multilineComments!!.second.find(currentLine, startIndex = position.col)
        if (match == null) {
          position = position.copy(col = currentLine.length)
          break
        } else {
          position = position.copy(col = match.range.last + 1)
          insideMultilineComment = false
        }
      }

      val start = position

      val match = combinedTokenSeparator.find(currentLine, startIndex = start.col)
      val end = if (match == null) { // capture the remaining string
        start.copy(col = currentLine.length - 1)
      } else {
        start.copy(col = match.range.first - 1)
      }

      if (start >= end) {
        // when start points to the end of the line which means match results in null.
        // OR
        // position points to a tokenSeparator; increment position and try again.
        position++
        continue
      }

      tokenStream.addToken(start, end)
      position = end
    }

    this.tokenStream = tokenStream
  }

  private fun fetchNextLine() {
    _currentLine = source.fetchLine(position.line)
  }

  private fun adjustPositionIfNeeded() {
    if (position.shouldAdvanceLine())
      position = position.nextLine()
  }

  private fun classifyToken(
    tokenStr: String,
    start: Position,
    end: Position
  ): Token {
    if (identifiers.matches(tokenStr)) {
      return Token.Identifier(tokenStr, start, end)
    }
    throw LexicalException("Cannot classify the token: $tokenStr")
  }

  private fun buildDisjunctRegex(): Regex {
    val patterns = mutableSetOf("\\s")

    operators?.let { patterns.add(it.pattern) }
    separators?.let { patterns.add(it.pattern) }
    singleLineComments?.let { patterns.add(it.pattern) }
    multilineComments?.let { (start, end) ->
      patterns.add(start.pattern)
      patterns.add(end.pattern)
    }

    return Regex(patterns.joinToString("|"))
  }

  private fun MutableList<Token>.addToken(
    start: Position,
    end: Position
  ) {
    val tokenStr = currentLine.substring(start..end)
    this.add(classifyToken(tokenStr, start, end))
  }

  private fun Position.shouldAdvanceLine(): Boolean {
    return this.col >= currentLine.length
  }

  private infix fun Position.isAt(pattern: Regex?): Boolean {
    return pattern
      ?.find(currentLine)
      ?.let { match ->
        return@let (match.range.first == position.col)
      } ?: false
  }
}

