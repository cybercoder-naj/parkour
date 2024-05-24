package io.github.cybercodernaj.parkour.core.datasource

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class StringSourceTest {

  @Test
  fun `reads lines from a string`() {
    val firstLine = "This is the first line"
    val secondLine = "This is the second line"
    val contents = "$firstLine\n$secondLine"

    val source = StringSource(contents)

    assertEquals(firstLine, source.fetchLine(0))
    assertEquals(secondLine, source.fetchLine(1))
    assertEquals(firstLine, source.fetchLine(0))
  }
}