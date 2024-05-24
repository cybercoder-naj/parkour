package io.github.cybercodernaj.parkour.core.lexer

import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals

class LexerTest {
  private val lexer = Lexer()

  @Test
  fun `empty string returns singleton list with EOF`() {
    val tokens = lexer.tokenize("", 0)

    assertContentEquals(listOf(Token.EOF), tokens)
  }
}