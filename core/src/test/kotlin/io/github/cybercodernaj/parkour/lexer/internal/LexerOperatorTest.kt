package io.github.cybercodernaj.parkour.lexer.internal

import io.github.cybercodernaj.parkour.datasource.StringSource
import io.github.cybercodernaj.parkour.testutils.assertTokens
import io.github.cybercodernaj.parkour.utils.Position
import org.junit.jupiter.api.Test

class LexerOperatorTest {
  private val lexer = Lexer(
    hardKeywords = setOf("val", "var"),
    operators = setOf("*", "**", "/", "//", "+", "-", "=", "==")
  )

  @Test
  fun `returns a series of valid tokens`() {
    lexer.source = StringSource("val diff = new - old")

    assertTokens(lexer, listOf(
      Token.Keyword(
        value = "val",
        start = Position(0, 0),
        end = Position(0, 2),
        soft = false
      ),
      Token.Identifier(
        value = "diff",
        start = Position(0, 4),
        end = Position(0, 7)
      ),
      Token.Operator(
        value = "=",
        start = Position(0, 9),
        end = Position(0, 9)
      ),
      Token.Identifier(
        value = "new",
        start = Position(0, 11),
        end = Position(0, 13)
      ),
      Token.Operator(
        value = "-",
        start = Position(0, 15),
        end = Position(0, 15)
      ),
      Token.Identifier(
        value = "old",
        start = Position(0, 17),
        end = Position(0, 19)
      )
    ))
  }

  @Test
  fun `returns longest possible match`() {
    lexer.source = StringSource("n1 * n2\nn3**n4\nn5// n6")

    assertTokens(lexer, listOf(
      Token.Identifier(
        value = "n1",
        start = Position(0, 0),
        end = Position(0, 1)
      ),
      Token.Operator(
        value = "*",
        start = Position(0, 3),
        end = Position(0, 3)
      ),
      Token.Identifier(
        value = "n2",
        start = Position(0, 5),
        end = Position(0, 6)
      ),
      Token.Identifier(
        value = "n3",
        start = Position(1, 0),
        end = Position(1, 1)
      ),
      Token.Operator(
        value = "**",
        start = Position(1, 2),
        end = Position(1, 3)
      ),
      Token.Identifier(
        value = "n4",
        start = Position(1, 4),
        end = Position(1, 5)
      ),
      Token.Identifier(
        value = "n5",
        start = Position(2, 0),
        end = Position(2, 1)
      ),
      Token.Operator(
        value = "//",
        start = Position(2, 2),
        end = Position(2, 3)
      ),
      Token.Identifier(
        value = "n6",
        start = Position(2, 5),
        end = Position(2, 6)
      ),
    ))
  }
}