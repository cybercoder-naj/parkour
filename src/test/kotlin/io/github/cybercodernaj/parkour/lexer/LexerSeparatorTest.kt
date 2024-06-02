package io.github.cybercodernaj.parkour.lexer

import io.github.cybercodernaj.parkour.datasource.StringSource
import io.github.cybercodernaj.parkour.testutils.assertTokens
import io.github.cybercodernaj.parkour.utils.Position
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LexerSeparatorTest {
  private val lexer = Lexer(separators = setOf("(", ")", "<", ">", ",", "."))

  @Test
  fun `returns a series of valid tokens`() {
    lexer.source = StringSource("List<String>(str1, str2)")

    val token = lexer.nextToken()
    assertEquals(
      Token.Identifier(
        name = "List",
        start = Position(0, 0),
        end = Position(0, 3)
      ),
      token
    )

    val token2 = lexer.nextToken()
    assertEquals(
      Token.Separator(
        name = "<",
        start = Position(0, 4),
        end = Position(0, 4)
      ),
      token2
    )

    val token3 = lexer.nextToken()
    assertEquals(
      Token.Identifier(
        name = "String",
        start = Position(0, 5),
        end = Position(0, 10)
      ),
      token3
    )

    val token4 = lexer.nextToken()
    assertEquals(
      Token.Separator(
        name = ">",
        start = Position(0, 11),
        end = Position(0, 11)
      ),
      token4
    )

    val token5 = lexer.nextToken()
    assertEquals(
      Token.Separator(
        name = "(",
        start = Position(0, 12),
        end = Position(0, 12)
      ),
      token5
    )

    val token6 = lexer.nextToken()
    assertEquals(
      Token.Identifier(
        name = "str1",
        start = Position(0, 13),
        end = Position(0, 16)
      ),
      token6
    )

    val token7 = lexer.nextToken()
    assertEquals(
      Token.Separator(
        name = ",",
        start = Position(0, 17),
        end = Position(0, 17)
      ),
      token7
    )

    val token8 = lexer.nextToken()
    assertEquals(
      Token.Identifier(
        name = "str2",
        start = Position(0, 19),
        end = Position(0, 22)
      ),
      token8
    )

    val token9 = lexer.nextToken()
    assertEquals(
      Token.Separator(
        name = ")",
        start = Position(0, 23),
        end = Position(0, 23)
      ),
      token9
    )
  }

  @Test
  fun `returns multiple separator tokens`() {
    lexer.source = StringSource("Array<List<Set<Int>>>")

    assertTokens(lexer, listOf(
      Token.Identifier("Array", start = Position(0, 0), end = Position(0, 4)),
      Token.Separator("<", start = Position(0, 5), end = Position(0, 5)),
      Token.Identifier("List", start = Position(0, 6), end = Position(0, 9)),
      Token.Separator("<", start = Position(0, 10), end = Position(0, 10)),
      Token.Identifier("Set", start = Position(0, 11), end = Position(0, 13)),
      Token.Separator("<", start = Position(0, 14), end = Position(0, 14)),
      Token.Identifier("Int", start = Position(0, 15), end = Position(0, 17)),
      Token.Separator(">", start = Position(0, 18), end = Position(0, 18)),
      Token.Separator(">", start = Position(0, 19), end = Position(0, 19)),
      Token.Separator(">", start = Position(0, 20), end = Position(0, 20)),
    ))
  }
}
