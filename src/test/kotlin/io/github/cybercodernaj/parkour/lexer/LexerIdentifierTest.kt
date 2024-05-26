package io.github.cybercodernaj.parkour.lexer

import io.github.cybercodernaj.parkour.datasource.StringSource
import io.github.cybercodernaj.parkour.utils.Position
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LexerIdentifierTest {
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
    assertEquals(
      Token.Identifier(
        name = "name",
        start = Position(0, 0),
        end = Position(0, 3)
      ),
      token
    )
  }

  @Test
  fun `first returns an identifier, then EOF`() {
    lexer.source = StringSource("name\n  \n  ")

    val token = lexer.nextToken()
    assertEquals(
      Token.Identifier(
        name = "name",
        start = Position(0, 0),
        end = Position(0, 3)
      ),
      token
    )

    val token2 = lexer.nextToken()
    assertEquals(Token.EOF, token2)
  }

  @Test
  fun `throws error on unidentifiable token`() {
    lexer.source = StringSource("0na@me")
    try {
      lexer.nextToken()
      fail("The token should not have been returned")
    } catch (_: Exception) {
    }
  }

  @Test
  fun `returns 3 identifiers`() {
    lexer.source = StringSource("name\n  name2\n\t\tname3")

    val token = lexer.nextToken()
    assertEquals(
      Token.Identifier(
        name = "name",
        start = Position(0, 0),
        end = Position(0, 3)
      ),
      token
    )

    val token2 = lexer.nextToken()
    assertEquals(
      Token.Identifier(
        name = "name2",
        start = Position(1, 2),
        end = Position(1, 6)
      ),
      token2
    )

    val token3 = lexer.nextToken()
    assertEquals(
      Token.Identifier(
        name = "name3",
        start = Position(2, 2),
        end = Position(2, 6)
      ),
      token3
    )
  }
}