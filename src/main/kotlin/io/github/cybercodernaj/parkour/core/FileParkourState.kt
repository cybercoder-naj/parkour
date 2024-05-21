package io.github.cybercodernaj.parkour.core

import java.io.BufferedReader
import java.io.File

internal class FileParkourState(
  file: File
): ParkourState() {
  private val bufferedReader: BufferedReader = BufferedReader(file.reader())

  override var pos: Pos = Pos(0, 0)
    set(value) {
      if (value.line > field.line)
        currentLine = bufferedReader.readLine()
      field = value
    }
  override var currentLine: String? = bufferedReader.readLine()
}