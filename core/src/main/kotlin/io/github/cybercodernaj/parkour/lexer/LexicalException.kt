package io.github.cybercodernaj.parkour.lexer

/**
 * This is thrown when there is an error when trying to tokenize a string.
 * There are several reasons why this may be thrown:
 *  1. When a token does not abide by the identifier regex rule.
 *  2. When a literal does not abide by the rules and constraints.
 *
 * @author Nishant Aanjaney Jalan
 * @since 0.1.0
 */
class LexicalException(
  message: String? = null,
  cause: Throwable? = null
) : Exception(message, cause)