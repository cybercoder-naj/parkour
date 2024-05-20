package io.github.cybercodernaj.parkour.core

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class ParkourStateTest {

  private lateinit var tempFile: File

  @BeforeEach
  fun setup() {
    tempFile = File.createTempFile("fileToParse", null)
  }

  @AfterEach
  fun teardown() {
    tempFile.delete()
  }

  @Test
  fun initStateWithString() {
    val state = ParkourState("abc")

    assertEquals(ParkourState.Pos(0, 0), state.pos)
    assertEquals(ParkourState.SrcType.STRING, state.srcType)
    assertEquals("abc", state.currentLine)
  }


  @Test
  fun initStateWithFile() {
    val firstLine = "This is the first line"
    val secondLine = "This is the second line"
    tempFile.writeText("$firstLine\n$secondLine")

    val state = ParkourState(tempFile)

    assertEquals(ParkourState.Pos(0, 0), state.pos)
    assertEquals(ParkourState.SrcType.FILE, state.srcType)
    assertEquals(firstLine, state.currentLine)

    state.pos = ParkourState.Pos(1, 0)
    assertEquals(secondLine, state.currentLine)
  }
}