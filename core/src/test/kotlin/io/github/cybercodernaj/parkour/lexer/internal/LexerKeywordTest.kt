package io.github.cybercodernaj.parkour.lexer.internal

import io.github.cybercodernaj.parkour.datasource.StringSource
import io.github.cybercodernaj.parkour.utils.Position
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LexerKeywordTest {
  private val lexer = Lexer(hardKeywords = listOf("class", "val", "var"))

  @Test
  fun `returns a keyword`() {
    lexer.source = StringSource("val")

    val token = lexer.nextToken()
    assertEquals(
      Token.Keyword(
        value = "val",
        start = Position(0, 0),
        end = Position(0, 2),
        soft = false
      ),
      token
    )
  }

  @Test
  fun `returns 2 keywords in same line`() {
    lexer.source = StringSource("val class")

    val token = lexer.nextToken()
    assertEquals(
      Token.Keyword(
        value = "val",
        start = Position(0, 0),
        end = Position(0, 2),
        soft = false
      ),
      token
    )

    val token2 = lexer.nextToken()
    assertEquals(
      Token.Keyword(
        value = "class",
        start = Position(0, 4),
        end = Position(0, 8),
        soft = false
      ),
      token2
    )
  }

  @Test
  fun `returns 2 keywords in different line`() {
    lexer.source = StringSource("val\n\tclass")

    val token = lexer.nextToken()
    assertEquals(
      Token.Keyword(
        value = "val",
        start = Position(0, 0),
        end = Position(0, 2),
        soft = false
      ),
      token
    )

    val token2 = lexer.nextToken()
    assertEquals(
      Token.Keyword(
        value = "class",
        start = Position(1, 1),
        end = Position(1, 5),
        soft = false
      ),
      token2
    )
  }

  @Test
  fun `returns a keyword then identifier then EOF`() {
    lexer.source = StringSource("val age")

    val token = lexer.nextToken()
    assertEquals(
      Token.Keyword(
        value = "val",
        start = Position(0, 0),
        end = Position(0, 2),
        soft = false
      ),
      token
    )

    val token2 = lexer.nextToken()
    assertEquals(
      Token.Identifier(
        value = "age",
        start = Position(0, 4),
        end = Position(0, 6)
      ),
      token2
    )

    val token3 = lexer.nextToken()
    assertEquals(Token.EOF, token3)
  }
}