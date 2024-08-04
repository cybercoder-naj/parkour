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
}

private data class TrieNode<T, U>(
  val children: MutableMap<T, TrieNode<T, U>>,
  var value: U?,
  var terminal: Boolean
) {
  constructor(): this(mutableMapOf(), null, false)
}
