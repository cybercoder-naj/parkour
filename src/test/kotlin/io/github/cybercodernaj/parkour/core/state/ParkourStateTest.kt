package io.github.cybercodernaj.parkour.core.state

import io.github.cybercodernaj.parkour.core.lexer.Lexer
import io.github.cybercodernaj.parkour.core.utils.Position
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
  fun `initialize state with string`() {
    val state = StringParkourState(lexer, "abc")

    assertEquals(Position(0, 0), state.pos)
    assertEquals("abc", state.currentLine)
  }


  @Test
  fun `initialize state with file`() {
    val firstLine = "This is the first line"
    val secondLine = "This is the second line"
    tempFile.writeText("$firstLine\n$secondLine")

    val state = FileParkourState(lexer, tempFile)

    assertEquals(Position(0, 0), state.pos)
    assertEquals(firstLine, state.currentLine)
  }
}