package io.github.cybercodernaj.parkour.core.datasource

class StringSource(
  contents: String
) : TextSource {
  private val lines = contents.lines()

  override fun fetchLine(lineNumber: Int): String? =
    if (lineNumber >= lines.size)
      null
    else
      lines[lineNumber]
}