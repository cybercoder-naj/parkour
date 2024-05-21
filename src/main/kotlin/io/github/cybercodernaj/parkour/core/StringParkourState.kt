package io.github.cybercodernaj.parkour.core

internal class StringParkourState(
  contents: String
) : ParkourState() {
  override var pos: Pos = Pos(0, 0)

  override var currentLine: String? = contents
}