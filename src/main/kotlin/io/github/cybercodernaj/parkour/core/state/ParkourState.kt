package io.github.cybercodernaj.parkour.core.state

import io.github.cybercodernaj.parkour.core.lexer.Lexer
import io.github.cybercodernaj.parkour.core.lexer.Token

/**
 * The [ParkourState] maintains the state of the parsing state.
 *
 * @author Nishant Aanjaney Jalan
 * @since 0.0.1
 */
internal abstract class ParkourState(
  private val lexer: Lexer
) {
  var pos: Pos = Pos(0, 0)
    private set(value) {
      if (value.line > field.line)
        updateCurrentLine()
      field = value
    }

  lateinit var currentLine: String
    private set

  /**
   * The list of tokens for the currentLine
   *
   * @author Nishant Aanjaney Jalan
   * @since 0.0.1
   */
  var inputTokens = listOf<Token>()
    private set

  /**
   * Each strategy of managing [ParkourState] needs to be able to get the next line.
   * Any time the state finishes parsing a line, it will ask for the (possibly null) next line.
   *
   * @author Nishant Aanjaney Jalan
   * @since 0.0.1
   */
  abstract fun nextLine(): String?

  protected fun initialize() {
    updateCurrentLine()
  }

  /**
   * Update the current line by making a call to [nextLine].
   * Also updates [inputTokens] in synchronisation.
   *
   * @author Nishant Aanjaney Jalan
   * @since 0.0.1
   */
  private fun updateCurrentLine() {
    currentLine = nextLine() ?: TODO("match(Token.EOF)")
    inputTokens = lexer.tokenize(currentLine)
  }

  /**
   * Similar to a [Pair] class of two integers.
   * Holds the line and column number in the contents defined by the strategies.
   *
   * @author Nishant Aanjaney Jalan
   * @since 0.0.1
   */
  data class Pos(
    val line: Int,
    val col: Int
  )
}