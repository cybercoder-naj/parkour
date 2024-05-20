package io.github.cybercodernaj.parkour.core

import java.io.BufferedReader
import java.io.File

/**
 * The [ParkourState] maintains the state of the parsing state.
 *
 * @author Nishant Aanjaney Jalan
 * @since 0.0.1
 */
internal class ParkourState private constructor() {

  internal lateinit var srcType: SrcType

  internal var pos: Pos = Pos(0, 0)
    set(value) {
      if (value.line > field.line && srcType == SrcType.FILE) {
        currentLine = bufReader.readLine()
      }
      field = value
    }

  internal var currentLine: String? = null

  private lateinit var bufReader: BufferedReader

  /**
   * Creates a [ParkourState] with the position pointing at (0, 0).
   * Accepts the entire string as is.
   *
   * @param contents - The string required to parse
   *
   * @author Nishant Aanjaney Jalan
   * @since 0.0.1
   */
  constructor(contents: String) : this() {
    srcType = SrcType.STRING
    currentLine = contents
  }

  /**
   * Creates a [ParkourState] with the position pointing at (0, 0).
   * Lazily reads the file line by line.
   *
   * @param file - the file to read from. The file must be writable.
   *
   * @author Nishant Aanjaney Jalan
   * @since 0.0.1
   */
  constructor(file: File) : this() {
    srcType = SrcType.FILE
    bufReader = BufferedReader(file.reader())
    currentLine = bufReader.readLine()
  }

  internal data class Pos(
    val line: Int,
    val col: Int
  )

  internal enum class SrcType {
    STRING, FILE
  }
}