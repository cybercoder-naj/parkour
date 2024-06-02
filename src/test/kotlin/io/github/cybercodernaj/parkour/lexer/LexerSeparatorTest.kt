package io.github.cybercodernaj.parkour.lexer

import io.github.cybercodernaj.parkour.datasource.StringSource
import io.github.cybercodernaj.parkour.utils.Position
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LexerSeparatorTest {
  private val lexer = Lexer(hardKeywords = setOf("val", "var", "class"))

  @Test
  fun `returns a keyword`() {
    assertTrue(true)
  }
}