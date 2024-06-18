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
}