# Package io.github.cybercodernaj.parkour.lexer

The lexer is responsible to convert the given string into a stream of Tokens.

The lexer takes in multiple settings via the `LexerBuilder` that configures how it behaves.
It performs lexical analysis on a line-by-line basis and returns the next unconsumed token.
A newline character **always** separates a token.

## How to make a Lexer config

You can build your customised Lexer with the `lexer` function.

```kotlin
fun main() {
  val myLexer = lexer {
     ignorePattern = Regex("""\s+""")
     singleLineComments = Regex("//")
     /* more configurations */
  }
}
```

Find all configuration options and in more detail in `LexerBuilder`. 