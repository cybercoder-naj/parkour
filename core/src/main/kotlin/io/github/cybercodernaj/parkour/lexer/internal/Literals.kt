package io.github.cybercodernaj.parkour.lexer.internal

import io.github.cybercodernaj.parkour.exceptions.LexicalException

/**
 * A configuration class to lex literals.
 * There are only three types of literals the lexer manages.
 * 1. Integer literals are normally lexed with a pure stream of numbers with underscores.
 * 2. Floating literals are normally lexed with a forced decimal point with optional exponentiation.
 * 3. String literals are normally lexed exact strings till it finds the original match.
 *
 * Additionally, escape sequences are required to input special characters inside string literals.
 *
 * @constructor creates a configuration of literals for the lexer.
 * @param integerLiteral a regex that detects an integer literal.
 * @param floatingLiteral a regex that detects a floating point number literal.
 * @param singleLineString a set of strings that denote the start and end enclosing strings.
 * The lexer will throw a [LexicalException] when a string literal is not terminated in the same line.
 * @param escapeSequences a list of regex that matches an escape sequence. On match, it will return a Char based on the string matched.
 *
 * @author Nishant Aanjaney Jalan
 * @since 0.1.0
 */
internal class Literals(
  internal val integerLiteral: Regex? = Regex("""[-+]?[0-9_]+"""),
  internal val floatingLiteral: Regex? = Regex("""[-+]?[0-9_]*\.[0-9_]+(?:[eE][-+]?[0-9_]+)?"""),
  internal val singleLineString: Set<String> = setOf("\"", "\'"),
  internal val escapeSequences: List<Pair<Regex, (String) -> Char>> = emptyList(),
)