package io.github.cybercodernaj.parkour.testutils

import io.github.cybercodernaj.parkour.lexer.Lexer
import io.github.cybercodernaj.parkour.lexer.Token
import kotlin.test.assertEquals

internal fun assertTokens(
  lexer: Lexer,
  tokens: List<Token>
) {
  for (token in tokens) {
    assertEquals(token, lexer.nextToken())
  }
}
