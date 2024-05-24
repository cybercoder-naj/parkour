package io.github.cybercodernaj.parkour.core.lexer

/**
 * The lexer is responsible to convert the given string into a stream of [Token]s.
 *
 * @author Nishant Aanjaney Jalan
 * @since 0.0.1
 */
internal class Lexer {
  /**
   * Performs lexical analysis on the string and returns a list of tokens.
   *
   * @param contents the string to analyze.
   * @param line the line number of the content to fill the start and ending positions
   *    of each token.
   * @see Token
   * @author Nishant Aanjaney Jalan
   * @since 0.0.1
   */
  fun tokenize(contents: String, line: Int): List<Token> {
    val tokens = mutableListOf<Token>()

    return tokens + Token.EOF
  }
}