package io.github.cybercodernaj.parkour.core.datasource

interface TextSource {
  fun fetchLine(lineNumber: Int): String?
}