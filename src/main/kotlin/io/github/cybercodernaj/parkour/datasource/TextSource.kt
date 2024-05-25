package io.github.cybercodernaj.parkour.datasource

internal interface TextSource {
  /**
   * @param lineNumber the line number to read from.
   * @return the line at the line number. If null, then the line number does not exist.
   * @author Nishant Aanjaney Jalan
   * @since 0.0.1
   */
  fun fetchLine(lineNumber: Int): String?
}