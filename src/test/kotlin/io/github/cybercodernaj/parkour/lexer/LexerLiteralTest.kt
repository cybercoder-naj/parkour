package io.github.cybercodernaj.parkour.lexer

import io.github.cybercodernaj.parkour.datasource.StringSource
import io.github.cybercodernaj.parkour.utils.Position
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LexerLiteralTest {
  private val lexer = Lexer()

  @Test
  fun `returns an int number`() {
    lexer.source = StringSource("12345")

    val token = lexer.nextToken()
    assertEquals(Token.Literal.IntLiteral(12345L, Position(0, 0), Position(0, 4)), token)
  }

  @Test
  fun `returns a negative int number`() {
    lexer.source = StringSource("-12345")

    val token = lexer.nextToken()
    assertEquals(Token.Literal.IntLiteral(-12345L, Position(0, 0), Position(0, 5)), token)
  }

  @Test
  fun `returns an int number with underscores`() {
    lexer.source = StringSource("3_00_000")

    val token = lexer.nextToken()
    assertEquals(Token.Literal.IntLiteral(300000L, Position(0, 0), Position(0, 7)), token)
  }

  @Test
  fun `returns a floating point number`() {
    lexer.source = StringSource("+12.42")

    val token = lexer.nextToken()
    assertEquals(Token.Literal.FloatLiteral(12.42, Position(0, 0), Position(0, 5)), token)
  }

  @Test
  fun `returns floating point with no leading digits`() {
    lexer.source = StringSource("0.32")

    val token = lexer.nextToken()
    assertEquals(Token.Literal.FloatLiteral(0.32, Position(0, 0), Position(0, 3)), token)
  }


  @Test
  fun `returns floating point with exponent digits`() {
    lexer.source = StringSource("1.609e-19")

    val token = lexer.nextToken()
    assertEquals(Token.Literal.FloatLiteral(1.609e-19, Position(0, 0), Position(0, 8)), token)
  }

  @Test
  fun `returns floating point with underscores`() {
    lexer.source = StringSource("1.6__0_9e-1__9")

    val token = lexer.nextToken()
    assertEquals(Token.Literal.FloatLiteral(1.609e-19, Position(0, 0), Position(0, 13)), token)
  }
}