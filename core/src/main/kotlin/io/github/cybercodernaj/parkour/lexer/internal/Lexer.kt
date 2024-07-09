package io.github.cybercodernaj.parkour.lexer.internal

import io.github.cybercodernaj.parkour.datasource.TextSource
import io.github.cybercodernaj.parkour.exceptions.LexicalException
import io.github.cybercodernaj.parkour.lexer.LexerBuilder
import io.github.cybercodernaj.parkour.utils.Position

/**
 * This class is internal. You can create an instance of this class by [LexerBuilder]
 *
 * @author Nishant Aanjaney Jalan
 * @since 0.1.0
 */
class Lexer internal constructor(
  private val ignorePattern: Regex = Defaults.ignorePattern,
  private val singleLineComments: Regex? = Defaults.singleLineComments,
  private val multilineComments: Pair<Regex, Regex>? = Defaults.multilineComments,
  private val identifiers: Regex = Defaults.identifiers,
  private val hardKeywords: List<String> = emptyList(),
  private val operators: List<String> = emptyList(),
  private val separators: List<String> = emptyList(),
  private val integerLiteral: Regex? = Defaults.integerLiteral,
  private val floatingLiteral: Regex? = Defaults.floatingLiteral,
  private val singleLineString: Set<String> = Defaults.singleLineString,
  private val escapeSequences: List<Pair<Regex, (String) -> Char>> = Defaults.escapeSequences,
) {
  /**
   * A list of common patterns and lists of items that most programming languages and
   * data serialization formats.
   *
   * @author Nishant Aanjaney Jalan
   * @since 0.2.0
   */
  object Defaults {
    /**
     * The lexer will skip over any strings that match this regex.
     * This acts like a token separator.
     *
     * @author Nishant Aanjaney Jalan
     * @since 0.2.0
     */
    val ignorePattern = Regex("""\s+""")
    val singleLineComments: Regex? = null
    val multilineComments: Pair<Regex, Regex>? = null
    val identifiers = Regex("""[a-zA-Z_]\w*""")
    val integerLiteral = Regex("""[-+]?[0-9_]+""")
    val floatingLiteral = Regex("""[-+]?[0-9_]*\.[0-9_]+(?:[eE][-+]?[0-9_]+)?""")
    val singleLineString: Set<String> = setOf("\"", "\'")
    val escapeSequences: List<Pair<Regex, (String) -> Char>> = emptyList() // TODO fill this list
  }

  private var position = Position(0, 0)

  internal lateinit var source: TextSource

  private var _currentLine: String? = null
  private val currentLine: String
    get() = _currentLine!!

  private var tokenIndex = 0
  private var tokenStream = emptyList<Token>()

  private var insideMultilineComment = false

  private val _separators = separators.sortedByDescending(String::length)
  private val _operators = operators.sortedByDescending(String::length)

  /**
   * Fetches the next [Token] from the source
   *
   * @author Nishant Aanjaney Jalan
   * @since 0.1.0
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
      val initial = position

      if (position pointsAt singleLineComments != null) {
        position = position.copy(col = currentLine.length)
        break
      }

      if (!insideMultilineComment && position pointsAt multilineComments?.first != null) {
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

      (position pointsAt ignorePattern)
        ?.let { match ->
          position = position.copy(col = match.range.last + 1)
        }

      (position pointsAtSome hardKeywords)
        ?.let { keyword ->
          val end = position.copy(col = position.col + keyword.length - 1)
          tokenStream.addHardKeyword(position, end)
          position = end + 1
        }

      tryLiterals()
        ?.let { token ->
          tokenStream.add(token)
          position = token.end + 1
        }

      (position pointsAtSome _operators)
        ?.let { keyword ->
          val end = position.copy(col = position.col + keyword.length - 1)
          tokenStream.addOperator(position, end)
          position = end + 1
        }

      (position pointsAtSome _separators)
        ?.let { keyword ->
          val end = position.copy(col = position.col + keyword.length - 1)
          tokenStream.addSeparator(position, end)
          position = end + 1
        }

      (position pointsAt identifiers)
        ?.let { match ->
          val end = position.copy(col = match.range.last)
          tokenStream.addIdentifier(position, end)
          position = end + 1
        }

      if (position == initial)
        throw LexicalException("Could not identify the given token.")
    }

    this.tokenStream = tokenStream
    adjustPositionIfNeeded()
  }

  private fun tryLiterals(): Token.Literal? {
    (position pointsAt floatingLiteral)
      ?.let { match ->
        if (match.value.isBlank())
          return null

        val end = position.copy(col = match.range.last)
        match.value
          .replace("_", "")
          .toDoubleOrNull()?.let { value ->
            return Token.Literal.FloatLiteral(value, position, end)
          } ?: throw LexicalException("Double regex is badly formed.")
      }

    (position pointsAt integerLiteral)
      ?.let { match ->
        if (match.value.isBlank())
          return null

        val end = position.copy(col = match.range.last)
        match.value
          .replace("_", "")
          .toLongOrNull()?.let { value ->
            return Token.Literal.IntLiteral(value, position, end)
          } ?: throw LexicalException("Int regex is badly formed. Tried parsing ${match.value} to an integer")
      }

    val stringStart = position pointsAtSome singleLineString
    if (stringStart != null) {
      val stringLit = StringBuilder().append(currentLine[position.col])
      val start = position++
      if (position.col >= currentLine.length)
        throw LexicalException("String not closed in the given line")
      while (currentLine[position.col].toString() != stringStart) {
        val matches = escapeSequences.mapNotNull { (regex, getEscapeChar) ->
          val result = (position pointsAt regex) ?: return@mapNotNull null
          result.value to getEscapeChar(result.value)
        }

        if (matches.isEmpty()) {
          stringLit.append(currentLine[position.col])
          position++
        } else {
          if (matches.size > 1) {
            // TODO add a warning about this
          }
          val (escapeSequence, equivalentChar) = matches[0]
          stringLit.append(equivalentChar)
          position += escapeSequence.length
        }

        if (position.col >= currentLine.length)
          throw LexicalException("String not closed in the given line")
      }
      stringLit.append(currentLine[position.col])
      return Token.Literal.StringLiteral(stringLit.toString(), start, position)
    }

    return null
  }

  private fun fetchNextLine() {
    _currentLine = source.fetchLine(position.line)
  }

  private fun adjustPositionIfNeeded() {
    if (position.shouldAdvanceLine())
      position = position.nextLine()
  }

  private fun MutableList<Token>.addIdentifier(
    start: Position,
    end: Position
  ) {
    val identifier = currentLine.substring(start..end)
    this.add(Token.Identifier(identifier, start, end))
  }

  private fun MutableList<Token>.addHardKeyword(
    start: Position,
    end: Position
  ) {
    val identifier = currentLine.substring(start..end)
    this.add(Token.Keyword(identifier, start, end))
  }

  private fun MutableList<Token>.addOperator(
    start: Position,
    end: Position
  ) {
    val identifier = currentLine.substring(start..end)
    this.add(Token.Operator(identifier, start, end))
  }

  private fun MutableList<Token>.addSeparator(
    start: Position,
    end: Position
  ) {
    val identifier = currentLine.substring(start..end)
    this.add(Token.Separator(identifier, start, end))
  }

  /**
   * @return true if currentLine is invalidated
   */
  private fun Position.shouldAdvanceLine(): Boolean {
    return this.col >= currentLine.length
  }

  /**
   * @param pattern the regex string to look for
   * @return a match if the [currentLine] follows the pattern at the [position]. Null if not.
   */
  private infix fun Position.pointsAt(pattern: Regex?): MatchResult? {
    return pattern
      ?.find(currentLine, startIndex = this.col)
      ?.let { match ->
        if (match.range.first == this.col)
          match
        else null
      }
  }

  /**
   * @param list a list of strings to search which is being pointed at
   * @return the string from the list that [currentLine] is being pointed at. Null if not.
   */
  private infix fun Position.pointsAtSome(list: Iterable<String>): String? {
    return list.find { this.pointsAt(it) }
  }

  /**
   * @param pattern the string to look for
   * @return true if the [currentLine] follows the [pattern] at the [position].
   */
  private infix fun Position.pointsAt(pattern: String): Boolean {
    return currentLine.startsWith(pattern, startIndex = this.col)
  }
}

