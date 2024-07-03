package io.github.cybercodernaj.parkour.lexer

import io.github.cybercodernaj.parkour.datasource.StringSource
import io.github.cybercodernaj.parkour.lexer.internal.Token
import io.github.cybercodernaj.parkour.testutils.assertTokens
import io.github.cybercodernaj.parkour.utils.Position
import org.junit.jupiter.api.Test

class LexerBuilderTest {
  @Test
  fun `initialise a default lexer`() {
    val myLexer = lexer {}
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
      multiLineComments(Regex("/\\*") to Regex("\\*/"))
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
}

