package io.github.cybercodernaj.parkour.core.state

internal class StringParkourState(
  contents: String
) : ParkourState() {
  override var pos = Pos(0, 0)

  override var currentLine: String? = contents
}