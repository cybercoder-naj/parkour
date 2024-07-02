package io.github.cybercodernaj.parkour.lexer.internal

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
 * @param hardKeywords A set of strings that are considered hard keywords.
 *    Hard keywords are a characters and symbols that give a particular meaning to a program.
 *    They may not be used as identifiers. (Default: [])
 * @param operators A set of strings that are considered as operators.
 *    Operators are characters and symbols that may perform arithmetic or logical operations. (Default: [])
 * @param separators A set of strings that are considered as separators.
 *    Separators are characters and symbols that act like delimiters to separate other meaningful elements. (Default: [])
 * @param literals The configuration of literals. Literals denote constant values
 * such as numbers, strings, and characters. (Default: see [Literals])
 *
 * @author Nishant Aanjaney Jalan
 * @since 0.1.0
 */
class Lexer internal constructor(
  private val ignorePattern: Regex = Regex("""\s+"""),
  private val singleLineComments: Regex? = null,
  private val multilineComments: Pair<Regex, Regex>? = null,
  private val identifiers: Regex = Regex("""[a-zA-Z_]\w*"""),
  private val hardKeywords: Set<String> = emptySet(),
  private val operators: Set<String> = emptySet(),
  private val separators: Set<String> = emptySet(),
  private val literals: Literals = Literals()
) {
  private var position = Position(0, 0)

  internal lateinit var source: TextSource

  private var _currentLine: String? = null
  private val currentLine: String
    get() = _currentLine!!

  private var tokenIndex = 0
  private var tokenStream = emptyList<Token>()

  private var insideMultilineComment = false

  private val _hardKeywords = hardKeywords.sortedByDescending(String::length)
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

      (position pointsAtSome _hardKeywords)
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
    (position pointsAt literals.floatingLiteral)
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

    (position pointsAt literals.integerLiteral)
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

    val stringStart = position pointsAtSome literals.singleLineString
    if (stringStart != null) {
      val stringLit = StringBuilder().append(currentLine[position.col])
      val start = position++
      if (position.col >= currentLine.length)
        throw LexicalException("String not closed in the given line")
      while (currentLine[position.col].toString() != stringStart) {
        val matches = literals.escapeSequences.mapNotNull { (regex, getEscapeChar) ->
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

