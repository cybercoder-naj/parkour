package io.github.cybercodernaj.parkour.core.lexer

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LexerTest {
  private val lexer = Lexer()

  @Test
  fun `empty string returns singleton list with EOF`() {
    val token = lexer.nextToken()

    assertEquals(Token.EOF, token)
  }
}