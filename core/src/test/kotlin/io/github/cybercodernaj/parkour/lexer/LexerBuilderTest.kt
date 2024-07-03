package io.github.cybercodernaj.parkour.lexer

import io.github.cybercodernaj.parkour.datasource.StringSource
import io.github.cybercodernaj.parkour.lexer.internal.Token
import io.github.cybercodernaj.parkour.testutils.assertTokens
import io.github.cybercodernaj.parkour.utils.Position
import org.junit.jupiter.api.Test

class LexerBuilderTest {
  @Test
  fun `initialise a default lexer`() {
    lexer {}
  }

  @Test
  fun `sets ignore patterns`() {
    val myLexer = lexer {
      ignorePattern(Regex("\\."))
    }

    myLexer.source = StringSource("hi.hello")
    assertTokens(
      myLexer,
      listOf(
        Token.Identifier("hi", Position(0, 0), Position(0, 1)),
        Token.Identifier("hello", Position(0, 3), Position(0, 7))
      )
    )
  }

  @Test
  fun `sets single line comments`() {
    val myLexer = lexer {
      singleLineComments(Regex("//"))
    }

    myLexer.source = StringSource("hi // hello\nhru")
    assertTokens(
      myLexer,
      listOf(
        Token.Identifier("hi", Position(0, 0), Position(0, 1)),
        Token.Identifier("hru", Position(1, 0), Position(1, 2))
      )
    )
  }

  @Test
  fun `sets multiline comments`() {
    val myLexer = lexer {
      multilineComments(Regex("/\\*") to Regex("\\*/"))
    }

    myLexer.source = StringSource("hi /* hello */\nhru")
    assertTokens(
      myLexer,
      listOf(
        Token.Identifier("hi", Position(0, 0), Position(0, 1)),
        Token.Identifier("hru", Position(1, 0), Position(1, 2))
      )
    )
  }

  @Test
  fun `sets identifiers rule`() {
    val myLexer = lexer {
      identifiers(Regex("[a-z][a-zA-Z0-9]+"))
    }

    myLexer.source = StringSource("hi value")
    assertTokens(
      myLexer,
      listOf(
        Token.Identifier("hi", Position(0, 0), Position(0, 1)),
        Token.Identifier("value", Position(0, 3), Position(0, 7))
      )
    )
  }

  @Test
  fun `sets hard keywords`() {
    val myLexer = lexer {
      hardKeywords("val", "var")
    }

    myLexer.source = StringSource("val c")
    assertTokens(
      myLexer,
      listOf(
        Token.Keyword("val", Position(0, 0), Position(0, 2)),
        Token.Identifier("c", Position(0, 4), Position(0, 4)),
      )
    )
  }
}

