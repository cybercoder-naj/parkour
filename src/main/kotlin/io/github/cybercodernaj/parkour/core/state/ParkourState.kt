package io.github.cybercodernaj.parkour.core.state

import io.github.cybercodernaj.parkour.core.lexer.Lexer
import io.github.cybercodernaj.parkour.core.lexer.Token
import io.github.cybercodernaj.parkour.core.utils.Position

/**
 * The [ParkourState] maintains the state of the parsing state.
 *
 * @author Nishant Aanjaney Jalan
 * @since 0.0.1
 */
internal abstract class ParkourState(
  private val lexer: Lexer
) {
  var pos: Position = Position(0, 0)
    private set(value) {
      if (value.line > field.line)
        updateCurrentLine()
      field = value
    }

  var currentLine: String? = null
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
   * When this function is invoked, it is assumed that [pos] is already updated
   * to the new position.
   *
   * @author Nishant Aanjaney Jalan
   * @since 0.0.1
   */
  private fun updateCurrentLine() {
    currentLine = nextLine()

    inputTokens = currentLine?.let { lexer.tokenize(it, pos.line) } ?: listOf(Token.EOF)
  }
}