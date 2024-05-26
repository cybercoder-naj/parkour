package io.github.cybercodernaj.parkour.lexer

import io.github.cybercodernaj.parkour.datasource.StringSource
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LexerTest {
  private val lexer = Lexer()

  @Test
  fun `returns EOF on empty source`() {
    lexer.source = StringSource("")
    val token = lexer.nextToken()

    assertEquals(Token.EOF, token)
  }

  @Test
  fun `returns an identifier`() {
    lexer.source = StringSource("name")
    val token = lexer.nextToken()

    assertTrue(token is Token.Identifier, "The token is not an identifier")
    assertEquals("name", token.name)
  }

  @Test
  fun `first returns an identifier, then EOF`() {
    lexer.source = StringSource("name\n  \n  ")
    val token = lexer.nextToken()

    assertTrue(token is Token.Identifier, "The token is not an identifier")
    assertEquals("name", token.name)

    val token2 = lexer.nextToken()

    assertEquals(Token.EOF, token2)
  }

  @Test
  fun `throws error on unidentifiable token`() {
    lexer.source = StringSource("0na@me")
    try {
      val token = lexer.nextToken()
      fail("The token should not have been returned")
    } catch (_: Exception) {
    }
  }
}