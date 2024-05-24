package io.github.cybercodernaj.parkour.core.state

import io.github.cybercodernaj.parkour.core.lexer.Lexer
import java.io.BufferedReader
import java.io.File

/**
 * Parses a file by reading from it line by line.
 *
 * @author Nishant Aanjaney Jalan
 * @since 0.0.1
 */
internal class FileParkourState(
  lexer: Lexer,
  file: File
) : ParkourState(lexer) {
  private val bufferedReader: BufferedReader = file.bufferedReader()

  init {
    initialize()
  }

  override fun nextLine(): String? =
    bufferedReader.readLine()
}