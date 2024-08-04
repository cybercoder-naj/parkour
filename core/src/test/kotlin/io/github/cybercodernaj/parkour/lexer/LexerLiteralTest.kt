package io.github.cybercodernaj.parkour.lexer

import io.github.cybercodernaj.parkour.datasource.StringSource
import io.github.cybercodernaj.parkour.exceptions.LexicalException
import io.github.cybercodernaj.parkour.testutils.assertTokens
import io.github.cybercodernaj.parkour.utils.Position
import org.junit.jupiter.api.Test
import kotlin.test.Ignore
import kotlin.test.assertEquals
import kotlin.test.fail

class LexerLiteralTest {
  private val lexer = Lexer(
    hardKeywords = setOf("char"),
    operators = setOf("="),
    literals = Literals(
      escapeSequences = listOf(
        Regex("""\\f""") to { _ -> 'f' },
        Regex("""\\n""") to { _ -> '\n' },
        Regex("""\\u\d{4}""") to { unicode -> unicode.substring(2).toInt(16).toChar() },
      )
    )
  )

  @Test
  fun `returns an int number`() {
    lexer.source = StringSource("12345")

    val token = lexer.nextToken()
    assertEquals(Token.IntLiteral(12345L, Position(0, 0), Position(0, 4)), token)
  }

  @Test
  fun `returns a negative int number`() {
    lexer.source = StringSource("-12345")

    val token = lexer.nextToken()
    assertEquals(Token.IntLiteral(-12345L, Position(0, 0), Position(0, 5)), token)
  }

  @Test
  @Ignore
  fun `returns an int number with underscores`() {
    lexer.source = StringSource("3_00_000")

    val token = lexer.nextToken()
    assertEquals(Token.IntLiteral(300000L, Position(0, 0), Position(0, 7)), token)
  }

  @Test
  fun `returns a floating point number`() {
    lexer.source = StringSource("+12.42")

    val token = lexer.nextToken()
    assertEquals(Token.FloatLiteral(12.42, Position(0, 0), Position(0, 5)), token)
  }

  @Test
  fun `returns floating point with no leading digits`() {
    lexer.source = StringSource("0.32")

    val token = lexer.nextToken()
    assertEquals(Token.FloatLiteral(0.32, Position(0, 0), Position(0, 3)), token)
  }


  @Test
  fun `returns floating point with exponent digits`() {
    lexer.source = StringSource("1.609e-19")

    val token = lexer.nextToken()
    assertEquals(Token.FloatLiteral(1.609e-19, Position(0, 0), Position(0, 8)), token)
  }

  @Test
  @Ignore
  fun `returns floating point with underscores`() {
    lexer.source = StringSource("1.6__0_9e-1__9")

    val token = lexer.nextToken()
    assertEquals(Token.FloatLiteral(1.609e-19, Position(0, 0), Position(0, 13)), token)
  }

  @Test
  fun `returns character literals`() {
    lexer.source = StringSource("char c = \'a\'")

    assertTokens(
      lexer,
      listOf(
        Token.Keyword("char", Position(0, 0), Position(0, 3)),
        Token.Identifier("c", Position(0, 5), Position(0, 5)),
        Token.Operator("=", Position(0, 7), Position(0, 7)),
        Token.StringLiteral("\'a\'", Position(0, 9), Position(0, 11))
      )
    )
  }

  @Test
  fun `returns string literals`() {
    lexer.source = StringSource("char c = \"abcde\"")

    assertTokens(
      lexer,
      listOf(
        Token.Keyword("char", Position(0, 0), Position(0, 3)),
        Token.Identifier("c", Position(0, 5), Position(0, 5)),
        Token.Operator("=", Position(0, 7), Position(0, 7)),
        Token.StringLiteral("\"abcde\"", Position(0, 9), Position(0, 15))
      )
    )
  }

  @Test
  fun `throws error on unclosed strings`() {
    lexer.source = StringSource("char c = \"abcde \n hi")

//    assertTokens(
//      lexer,
//      listOf(
//        Token.Keyword("char", Position(0, 0), Position(0, 3)),
//        Token.Identifier("c", Position(0, 5), Position(0, 5)),
//        Token.Operator("=", Position(0, 7), Position(0, 7)),
//      )
//    )

    try {
      lexer.nextToken()
      fail("Should have returned an error")
    } catch (e: LexicalException) {

    }
  }

  @Test
  fun `returns escaped characters`() {
    lexer.source = StringSource("'\\u1234' '\\n' '\\f'")

    assertTokens(
      lexer,
      listOf(
        Token.StringLiteral("'\u1234'", Position(0, 0), Position(0, 7)),
        Token.StringLiteral("'\n'", Position(0, 9), Position(0, 12)),
        Token.StringLiteral("'f'", Position(0, 14), Position(0, 17)),
      )
    )
  }
}