package io.github.cybercodernaj.parkour.lexer.internal

import io.github.cybercodernaj.parkour.datasource.StringSource
import io.github.cybercodernaj.parkour.testutils.assertTokens
import io.github.cybercodernaj.parkour.utils.Position
import org.junit.jupiter.api.Test

class LexerSeparatorTest {
  private val lexer = Lexer(separators = listOf("(", ")", "<", ">", ",", "."))

  @Test
  fun `returns a series of valid tokens`() {
    lexer.source = StringSource("List<String>(str1, str2)")

    assertTokens(lexer, listOf(
      Token.Identifier(
        value = "List",
        start = Position(0, 0),
        end = Position(0, 3)
      ),
      Token.Separator(
        value = "<",
        start = Position(0, 4),
        end = Position(0, 4)
      ),
      Token.Identifier(
        value = "String",
        start = Position(0, 5),
        end = Position(0, 10)
      ),
      Token.Separator(
        value = ">",
        start = Position(0, 11),
        end = Position(0, 11)
      ),
      Token.Separator(
        value = "(",
        start = Position(0, 12),
        end = Position(0, 12)
      ),
      Token.Identifier(
        value = "str1",
        start = Position(0, 13),
        end = Position(0, 16)
      ),
      Token.Separator(
        value = ",",
        start = Position(0, 17),
        end = Position(0, 17)
      ),
      Token.Identifier(
        value = "str2",
        start = Position(0, 19),
        end = Position(0, 22)
      ),
      Token.Separator(
        value = ")",
        start = Position(0, 23),
        end = Position(0, 23)
      ),
    ))
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
