package io.github.cybercodernaj.parkour.core.state

import io.github.cybercodernaj.parkour.core.lexer.Lexer

/**
 * Tries to parse the whole string as a whole.
 *
 * @author Nishant Aanjaney Jalan
 * @since 0.0.1
 */
internal class StringParkourState(
  lexer: Lexer,
  contents: String
) : ParkourState(lexer) {
  private val lines = contents.lines()

  init {
    initialize()
  }

  override fun nextLine(): String? {
    val (line, _) = pos

    return if (line !in lines.indices)
      null
    else lines[line]
  }
}