package io.github.cybercodernaj.parkour.core.state

import io.github.cybercodernaj.parkour.core.lexer.Lexer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class ParkourStateTest {

  private lateinit var tempFile: File
  private val lexer = Lexer()

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
    val state = StringParkourState(lexer, "abc")

    assertEquals(ParkourState.Pos(0, 0), state.pos)
    assertEquals("abc", state.currentLine)
  }


  @Test
  fun initStateWithFile() {
    val firstLine = "This is the first line"
    val secondLine = "This is the second line"
    tempFile.writeText("$firstLine\n$secondLine")

    val state = FileParkourState(lexer, tempFile)

    assertEquals(ParkourState.Pos(0, 0), state.pos)
    assertEquals(firstLine, state.currentLine)
  }
}