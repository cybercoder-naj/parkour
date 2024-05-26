package io.github.cybercodernaj.parkour.lexer

import io.github.cybercodernaj.parkour.datasource.TextSource
import io.github.cybercodernaj.parkour.utils.Position

/**
 * The lexer is responsible to convert the given string into a stream of [Token]s.
 * The lexer take in multiple settings that configure how it behaves.
 * It will perform lexical analysis on a line-by-line basis and return the next unconsumed token.
 *
 * @constructor Creates a lexer with the provided properties.
 * @property tokenSeparator The regex to split a given line to the lexer.
 *    Newline characters are **always** token separators. (Default: "\s")
 * @property singleLineComments The string that defines how a single-line comment starts.
 *    Once identified, the lexer will skip the remaining line. (Default: null)
 * @property multilineComments A pair of strings, the starting pattern and the ending pattern for a
 *    multiline comment block. (Default: null)
 * @property identifiers A regex string that defines the rules for identifying a name. (Default: "[a-zA-Z_]\w*")
 * @property keywords A list of strings that are considered as keywords. (Default: [])
 * @property operators A list of strings that are considered as operators. (Default: [])
 * @property separators A list of strings that are considered as separators. (Default: [])
 * @property literals A configuration of the literals to be considered. (Default: see [Literals])
 *
 * @author Nishant Aanjaney Jalan
 * @since 0.0.1
 */
class Lexer(
  private val tokenSeparator: Regex = Regex("""\s"""),
  private val singleLineComments: String? = null,
  private val multilineComments: Pair<String, String>? = null,
  private val identifiers: Regex = Regex("""[a-zA-Z_]\w*"""),
  private val keywords: List<String> = emptyList(),
  private val operators: List<String> = emptyList(),
  private val separators: List<String> = emptyList(),
  private val literals: Literals = Literals()
) {
  internal var position = Position(0, 0)
    private set(value) {
      if (value.shouldAdvanceLine()) {
        tokenInvalidation = true
      }
      field = value
    }

  internal lateinit var source: TextSource

  private var _currentLine: String? = null
  private val currentLine: String
    get() = _currentLine!!

  private var tokenIndex = 0
  private var tokenStream = emptyList<Token>()
  private var tokenInvalidation = true

  /**
   * Fetches the next [Token] from the source
   *
   * @author Nishant Aanjaney Jalan
   * @since 0.0.1
   */
  internal fun nextToken(): Token {
    if (tokenInvalidation)
      updateTokenStream()

    if (tokenStream.isEmpty()) {
      position = position.nextLine()
      return nextToken()
    }

    return tokenStream[tokenIndex++]
  }

  private fun updateTokenStream() {
    _currentLine = source.fetchLine(position.line)
    tokenIndex = 0 // reset the counter

    if (_currentLine == null) {
      tokenStream = listOf(Token.EOF)
      return
    }

    val tokenStream = mutableListOf<Token>()
    while (position.col < currentLine.length) {
      skipOverComments()
      val start = position

      val match = tokenSeparator.find(currentLine, startIndex = start.col)
      val end = if (match == null) { // capture the remaining string
        start.copy(col = currentLine.length - 1)
      } else {
        start.copy(col = match.range.last - 1)
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

    adjustPositionIfNeeded()
    this.tokenStream = tokenStream
  }

  private fun skipOverComments() {
    if (singleLineComments != null) {
      if (currentLine.substring(position.col).startsWith(singleLineComments)) {
        // point position to outside the line
        position = position.copy(col = currentLine.length)
      }
    }
  }

  private fun adjustPositionIfNeeded() {
    if (position.shouldAdvanceLine())
      position = position.nextLine()
  }

  private fun classifyToken(
    string: String,
    start: Position,
    end: Position
  ): Token {
    if (identifiers.matches(string)) {
      return Token.Identifier(string, start, end)
    }
    // TODO replace this with a custom exception
    throw Exception("Lexical error: Cannot classify the token: $string")
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
}

