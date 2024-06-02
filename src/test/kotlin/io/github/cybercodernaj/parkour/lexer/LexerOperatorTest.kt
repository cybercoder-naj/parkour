package io.github.cybercodernaj.parkour.lexer

import io.github.cybercodernaj.parkour.datasource.StringSource
import io.github.cybercodernaj.parkour.utils.Position
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LexerOperatorTest {
  private val lexer = Lexer(
    hardKeywords = setOf("val", "var"),
    operators = setOf("*", "**", "/", "//", "+", "-", "=", "==")
  )

  @Test
  fun `returns a series of valid tokens`() {
    lexer.source = StringSource("val diff = new - old")

    val token = lexer.nextToken()
    assertEquals(
      Token.Keyword(
        name = "val",
        start = Position(0, 0),
        end = Position(0, 2),
        soft = false
      ),
      token
    )

    val token2 = lexer.nextToken()
    assertEquals(
      Token.Identifier(
        name = "diff",
        start = Position(0, 4),
        end = Position(0, 7)
      ),
      token2
    )

    val token3 = lexer.nextToken()
    assertEquals(
      Token.Operator(
        name = "=",
        start = Position(0, 9),
        end = Position(0, 9)
      ),
      token3
    )

    val token4 = lexer.nextToken()
    assertEquals(
      Token.Identifier(
        name = "new",
        start = Position(0, 11),
        end = Position(0, 13)
      ),
      token4
    )

    val token5 = lexer.nextToken()
    assertEquals(
      Token.Operator(
        name = "-",
        start = Position(0, 15),
        end = Position(0, 15)
      ),
      token5
    )

    val token6 = lexer.nextToken()
    assertEquals(
      Token.Identifier(
        name = "old",
        start = Position(0, 17),
        end = Position(0, 19)
      ),
      token6
    )
  }

  @Test
  fun `returns longest possible match`() {
    lexer.source = StringSource("n1 * n2\nn3**n4\nn5// n6")

    val token = lexer.nextToken()
    assertEquals(
      Token.Identifier(
        name = "n1",
        start = Position(0, 0),
        end = Position(0, 1)
      ),
      token
    )

    val token2 = lexer.nextToken()
    assertEquals(
      Token.Operator(
        name = "*",
        start = Position(0, 3),
        end = Position(0, 3)
      ),
      token2
    )

    val token3 = lexer.nextToken()
    assertEquals(
      Token.Identifier(
        name = "n2",
        start = Position(0, 5),
        end = Position(0, 6)
      ),
      token3
    )

    val token4 = lexer.nextToken()
    assertEquals(
      Token.Identifier(
        name = "n3",
        start = Position(1, 0),
        end = Position(1, 1)
      ),
      token4
    )

    val token5 = lexer.nextToken()
    assertEquals(
      Token.Operator(
        name = "**",
        start = Position(1, 2),
        end = Position(1, 3)
      ),
      token5
    )

    val token6 = lexer.nextToken()
    assertEquals(
      Token.Identifier(
        name = "n4",
        start = Position(1, 4),
        end = Position(1, 5)
      ),
      token6
    )

    val token7 = lexer.nextToken()
    assertEquals(
      Token.Identifier(
        name = "n5",
        start = Position(2, 0),
        end = Position(2, 1)
      ),
      token7
    )

    val token8 = lexer.nextToken()
    assertEquals(
      Token.Operator(
        name = "//",
        start = Position(2, 2),
        end = Position(2, 3)
      ),
      token8
    )

    val token9 = lexer.nextToken()
    assertEquals(
      Token.Identifier(
        name = "n6",
        start = Position(2, 5),
        end = Position(2, 6)
      ),
      token9
    )
  }
}