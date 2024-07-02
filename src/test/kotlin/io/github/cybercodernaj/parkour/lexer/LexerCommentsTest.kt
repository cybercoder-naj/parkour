package io.github.cybercodernaj.parkour.lexer

import io.github.cybercodernaj.parkour.datasource.StringSource
import io.github.cybercodernaj.parkour.utils.Position
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LexerCommentsTest {
  private val lexer = Lexer(
    singleLineComments = Regex("//"),
    multilineComments = Regex("/\\*") to Regex("\\*/")
  )

  @Test
  fun `returns EOF when only single line comment`() {
    lexer.source = StringSource("// This is a comment")

    val token = lexer.nextToken()
    assertEquals(Token.EOF, token)
  }

  @Test
  fun `returns an identifier without a space`() {
    lexer.source = StringSource("age// Should still consider an identifier")

    val token = lexer.nextToken()
    assertEquals(
      Token.Identifier(
        value = "age",
        start = Position(0, 0),
        end = Position(0, 2)
      ),
      token
    )
  }

  @Test
  fun `returns 2 identifiers ignoring single line comment`() {
    lexer.source = StringSource("age // This is age\nhello")

    val token = lexer.nextToken()
    assertEquals(
      Token.Identifier(
        value = "age",
        start = Position(0, 0),
        end = Position(0, 2)
      ),
      token
    )

    val token2 = lexer.nextToken()
    assertEquals(
      Token.Identifier(
        value = "hello",
        start = Position(1, 0),
        end = Position(1, 4)
      ),
      token2
    )
  }

  @Test
  fun `returns 2 identifiers ignoring multi-line comment`() {
    lexer.source = StringSource("age /* This is age */\nhello")

    val token = lexer.nextToken()
    assertEquals(
      Token.Identifier(
        value = "age",
        start = Position(0, 0),
        end = Position(0, 2)
      ),
      token
    )

    val token2 = lexer.nextToken()
    assertEquals(
      Token.Identifier(
        value = "hello",
        start = Position(1, 0),
        end = Position(1, 4)
      ),
      token2
    )
  }

  @Test
  fun `returns 2 identifiers ignoring multi-line comment across lines`() {
    val content = """age
      |/* This is
      | * is new
      | * multiline
      | * comment
      | */
      |hello
    """.trimMargin()
    lexer.source = StringSource(content)

    val token = lexer.nextToken()
    assertEquals(
      Token.Identifier(
        value = "age",
        start = Position(0, 0),
        end = Position(0, 2)
      ),
      token
    )

    val token2 = lexer.nextToken()
    assertEquals(
      Token.Identifier(
        value = "hello",
        start = Position(6, 0),
        end = Position(6, 4)
      ),
      token2
    )
  }

  @Test
  fun `returns EOF when multiline comment is missing end symbol`() {
    val content = """age
      |/* This is
      | * is new
      | * multiline
      | * comment
      |hello
    """.trimMargin()
    lexer.source = StringSource(content)

    val token = lexer.nextToken()
    assertEquals(
      Token.Identifier(
        value = "age",
        start = Position(0, 0),
        end = Position(0, 2)
      ),
      token
    )

    val token2 = lexer.nextToken()
    assertEquals(Token.EOF, token2)
  }
}