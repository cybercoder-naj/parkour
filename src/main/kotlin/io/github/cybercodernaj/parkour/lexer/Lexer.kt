package io.github.cybercodernaj.parkour.lexer

import io.github.cybercodernaj.parkour.datasource.TextSource
import io.github.cybercodernaj.parkour.exceptions.LexicalException
import io.github.cybercodernaj.parkour.utils.Position

/**
 * The lexer is responsible to convert the given string into a stream of [Token]s.
 * The lexer take in multiple settings that configure how it behaves.
 * It will perform lexical analysis on a line-by-line basis and return the next unconsumed token.
 * Tokens are separated by the Regex, "\s".
 *
 * @constructor Creates a lexer with the provided properties.
 * @param singleLineComments The string that defines how a single-line comment starts.
 *    Once identified, the lexer will skip the remaining line. (Default: null)
 * @param multilineComments A pair of strings, the starting pattern and the ending pattern for a
 *    multiline comment block. (Default: null)
 * @param identifiers A regex string that defines the rules for identifying a name. (Default: "[a-zA-Z_]\w*")
 * @param keywords A list of strings that are considered as keywords. (Default: [])
 * @param operators A list of strings that are considered as operators. (Default: [])
 * @param separators A list of strings that are considered as separators. (Default: [])
 * @param literals A configuration of the literals to be considered. (Default: see [Literals])
 *
 * @author Nishant Aanjaney Jalan
 * @since 0.0.1
 */
class Lexer(
  /*
   * @param tokenSeparator The regex to split a given line to the lexer.
   *    Newline characters are **always** token separators. (Default: "\s")
   * private val tokenSeparator: Regex = Regex("""\s"""),
   */
  private val singleLineComments: Regex? = null,
  private val multilineComments: Pair<Regex, Regex>? = null,
  private val identifiers: Regex = Regex("""[a-zA-Z_]\w*"""),
  private val keywords: List<String> = emptyList(),
  private val operators: Regex? = null,
  private val separators: Regex? = null,
  private val literals: Literals = Literals()
) {
  private var position = Position(0, 0)
    private set(value) {
      if (value.shouldAdvanceLine()) {
        areTokensInvalid = true
      }
      // TODO if position moves to new line, then automatically fetch the next line.
      //    Also removes the need for tokenInvalidation (maybe).
      //    This change breaks the test cases currently. Requires debugging.
      field = value
    }

  internal lateinit var source: TextSource

  private var _currentLine: String? = null
  private val currentLine: String
    get() = _currentLine!!

  private var tokenIndex = 0
  private var tokenStream = emptyList<Token>()
  private var areTokensInvalid = true

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
    if (areTokensInvalid)
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
      skipOverComments()
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

  private fun skipOverComments() {
    singleLineComments
      ?.find(currentLine) // find the comment
      ?.let { match ->
        if (match.range.first == position.col) { // assert comment is at the position
          // point position to outside the line
          position = position.copy(col = currentLine.length)
        }
      }

    multilineComments
      ?.let { (start, end) ->
        if (!insideMultilineComment) {
          start.find(currentLine)?.let inner@{ match ->
            if (match.range.first != position.col) // valid only if position points to the start of the comment
              return@inner

            insideMultilineComment = true
          }
        }

        if (insideMultilineComment) {
          val match = end.find(currentLine, startIndex = position.col)
          if (match == null) {
            position = position.copy(col = currentLine.length)
          } else {
            position = position.copy(col = match.range.last + 1)
            insideMultilineComment = false
          }
        }
      }
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
}

