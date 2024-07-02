package io.github.cybercodernaj.parkour.state

import io.github.cybercodernaj.parkour.lexer.internal.Lexer

/**
 * The [ParkourState] maintains the state of the parsing state.
 *
 * @author Nishant Aanjaney Jalan
 * @since 0.1.0
 */
internal abstract class ParkourState(
  private val lexer: Lexer,
) {
  fun identifier(): String {
    val token = lexer.nextToken()
    // TODO assert that the token is an identifier
    TODO("return the string inside the token")
  }
}