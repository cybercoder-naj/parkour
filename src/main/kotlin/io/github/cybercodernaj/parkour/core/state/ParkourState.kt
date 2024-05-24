package io.github.cybercodernaj.parkour.core.state

/**
 * The [ParkourState] maintains the state of the parsing state.
 * Accepts the entire string as is.
 *
 * @author Nishant Aanjaney Jalan
 * @since 0.0.1
 */
internal abstract class ParkourState {
  abstract var pos: Pos
  abstract var currentLine: String?

  internal data class Pos(
    val line: Int,
    val col: Int
  )
}