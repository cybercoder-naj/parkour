package io.github.cybercodernaj.parkour.core.datasource

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class FileSourceTest {

  private lateinit var file: File

  @BeforeEach
  fun setup() {
    file = File.createTempFile("fileToLex", null)
  }

  @AfterEach
  fun teardown() {
    file.delete()
  }

  @Test
  fun `reads lines from a file`() {
    val firstLine = "This is the first line"
    val secondLine = "This is the second line"
    file.writeText("$firstLine\n$secondLine")

    val source = FileSource(file)

    assertEquals(firstLine, source.fetchLine(0))
    assertEquals(secondLine, source.fetchLine(1))
    assertEquals(firstLine, source.fetchLine(0))
  }
}