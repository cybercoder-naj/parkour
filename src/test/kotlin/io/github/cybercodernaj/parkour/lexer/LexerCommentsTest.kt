package io.github.cybercodernaj.parkour.lexer

import io.github.cybercodernaj.parkour.datasource.StringSource
import io.github.cybercodernaj.parkour.utils.Position
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LexerCommentsTest {
  private val lexer = Lexer(
    singleLineComments = "//",
    multilineComments = "/*" to "*/"
  )

  @Test
  fun `returns EOF when only single line comment`() {
    lexer.source = StringSource("// This is a comment")

    val token = lexer.nextToken()
    assertEquals(Token.EOF, token)
  }

  @Test
  fun `returns 2 identifiers ignoring single line comment`() {
    lexer.source = StringSource("age // This is age\nhello")

    val token = lexer.nextToken()
    assertEquals(
      Token.Identifier(
        name = "age",
        start = Position(0, 0),
        end = Position(0, 2)
      ),
      token
    )

    val token2 = lexer.nextToken()
    assertEquals(
      Token.Identifier(
        name = "hello",
        start = Position(1, 0),
        end = Position(1, 4)
      ),
      token2
    )
  }
}