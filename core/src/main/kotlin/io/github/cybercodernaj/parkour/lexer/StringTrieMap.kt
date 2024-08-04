package io.github.cybercodernaj.parkour.lexer

internal class StringTrieMap<V> {
  private val root = TrieNode<Char, V>()

  operator fun set(key: String, value: V) {
    var node = root
    for (elem in key) {
      if (elem !in node.children) {
        node.children[elem] = TrieNode()
      }
      node = node.children[elem]!!
    }
    node.value = value
    node.terminal = true
  }

  operator fun get(key: String): V? {
    var node = root
    for (elem in key) {
      if (elem !in node.children)
        return null
      node = node.children[elem]!!
    }
    return if (node.terminal) node.value else null
  }

  /**
   * The [content] will walk down the trie to find a terminal value with the highest depth.
   *
   * @param content the line to find the longest match
   * @return the terminal value and the depth of trie traversed. Null if not found
   */
  fun getLongest(content: String): Pair<V, Int>? {
    var node = root
    var candidate: V? = null
    var depth = 0
    for (elem in content) {
      if (elem !in node.children) {
        return candidate?.let { it to depth }
      }
      node = node.children[elem]!!
      depth++
      if (node.terminal) {
        candidate = node.value!!
      }
    }
    return candidate?.let { it to depth }
  }
}

private data class TrieNode<T, U>(
  val children: MutableMap<T, TrieNode<T, U>>,
  var value: U?,
  var terminal: Boolean
) {
  constructor(): this(mutableMapOf(), null, false)
}
