package io.github.cybercodernaj.parkour.core.datasource

import java.io.File

class FileSource(
  file: File
): TextSource {
  private val bufferedReader = file.bufferedReader()
  private val lines = mutableListOf<String>()

  override fun fetchLine(lineNumber: Int): String? {
    if (lineNumber >= lines.size) {
      val line = bufferedReader.readLine()
      if (line == null)
        return null

      lines.add(line)
    }

    return lines[lineNumber]
  }
}