package io.github.cybercodernaj.parkour.lexer

import io.github.cybercodernaj.parkour.datasource.TextSource
import io.github.cybercodernaj.parkour.utils.Position

/**
 * The lexer is responsible to convert the given string into a stream of [Token]s.
 * The lexer take in multiple settings that configure how it behaves.
 * It will perform lexical analysis on a line-by-line basis and return the next unconsumed token.
 *
 * @constructor Creates a lexer with the provided properties.
 * @property tokenSeparator The regex to split a given line to the lexer. (Default: "\s")
 * @property singleLineComments The string that defines how a single-line comment starts.
 *    Once identified, the lexer will skip the entire line. (Default: null)
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
    private set

  internal lateinit var source: TextSource

  private var _currentLine: String? = null
  private val currentLine: String
    get() = _currentLine!!

  /**
   * Fetches the next [Token] from the source
   *
   * @author Nishant Aanjaney Jalan
   * @since 0.0.1
   */
  internal fun nextToken(): Token {
    validateCurrentLine()
    if (_currentLine == null)
      return Token.EOF

    if (currentLine.isBlank()) {
      position += currentLine.length
      return nextToken()
    }

    val start = position
    val buffer = StringBuilder()
    while (position.col < currentLine.length && !tokenSeparator.matches(currentLine[position.col].toString())) {
      buffer.append(currentLine[position.col])
      position++
    }
    val end = position

    return classifyToken(buffer.toString(), start, end)
  }

  private fun validateCurrentLine() {
    if (_currentLine == null) {
      updateCurrentLine()
    } else if (position.col >= currentLine.length) {
      position = position.nextLine()
      updateCurrentLine()
    }
  }

  private fun updateCurrentLine() {
    _currentLine = source.fetchLine(position.line)
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
}