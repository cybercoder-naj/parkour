package io.github.cybercodernaj.parkour.core.lexer

/**
 * The lexer is responsible to convert the given string into a stream of [Token]s.
 * The lexer take in multiple settings that configure how it behaves.
 *
 * @author Nishant Aanjaney Jalan
 * @since 0.0.1
 */
class Lexer(
  /**
   * The characters the lexer should not consider. (Default: "\s")
   *
   * @author Nishant Aanjaney Jalan
   * @since 0.0.1
   */
  val skipCharacters: Regex = Regex("\\s"),
  /**
   * The string that defines how a single-line comment starts.
   * Once identified, the lexer will skip the entire line. (Default: null)
   *
   * @author Nishant Aanjaney Jalan
   * @since 0.0.1
   */
  val singleLineComments: String? = null,
  /**
   * A pair of strings, the starting pattern and the ending pattern for a
   * multiline comment block. (Default: null)
   *
   * @author Nishant Aanjaney Jalan
   * @since 0.0.1
   */
  val multilineComments: Pair<String, String>? = null,
  /**
   * A regex string that defines the rules for identifying a name. (Default: "[a-zA-Z_]\w*")
   *
   * @author Nishant Aanjaney Jalan
   * @since 0.0.1
   */
  val identifiers: Regex = Regex("[a-zA-Z_]\\w*"),
  /**
   * A list of strings that are considered as keywords. (Default: [])
   *
   * @author Nishant Aanjaney Jalan
   * @since 0.0.1
   */
  val keywords: List<String> = emptyList<String>(),
  /**
   * A list of strings that are considered as operators. (Default: [])
   *
   * @author Nishant Aanjaney Jalan
   * @since 0.0.1
   */
  val operators: List<String> = emptyList<String>(),
  /**
   * A list of strings that are considered as separators. (Default: [])
   *
   * @author Nishant Aanjaney Jalan
   * @since 0.0.1
   */
  val separators: List<String> = emptyList<String>(),
  /**
   * A configuration of the literals to be considered. (Default: see [Literals])
   *
   * @author Nishant Aanjaney Jalan
   * @since 0.0.1
   */
  val literals: Literals = Literals()
) {
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
  internal fun tokenize(contents: String, line: Int): List<Token> {
    val tokens = mutableListOf<Token>()

    return tokens + Token.EOF
  }
}