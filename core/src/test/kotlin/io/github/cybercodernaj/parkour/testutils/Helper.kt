package io.github.cybercodernaj.parkour.testutils

import io.github.cybercodernaj.parkour.lexer.internal.Lexer
import io.github.cybercodernaj.parkour.lexer.internal.Token
import kotlin.test.assertEquals

internal fun assertTokens(
  lexer: Lexer,
  tokens: List<Token>
) {
  for (token in tokens) {
    assertEquals(token, lexer.nextToken())
  }
}
