import org.jetbrains.dokka.gradle.DokkaTask
import java.net.URI

plugins {
  kotlin("jvm") version "2.0.0"
  alias(libs.plugins.dokka)
  `java-library`
}

group = "io.github.cybercodernaj"
version = libs.versions.lib.get()

val docsDir = layout.projectDirectory.dir("docs/")

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(kotlin("test"))
}

tasks.test {
  useJUnitPlatform()
}
kotlin {
  jvmToolchain(18)
}

tasks.withType<DokkaTask>().configureEach {
  moduleName.set(project.name)
  moduleVersion.set(project.version.toString())
  outputDirectory.set(docsDir)
  dokkaSourceSets.configureEach {
    sourceLink {
      localDirectory.set(projectDir.resolve("src"))
      remoteUrl.set(URI.create("https://github.com/cybercoder-naj/Parkour").toURL())
      remoteLineSuffix.set("#L")
    }
  }
}

tasks.clean {
  delete = setOf(docsDir, layout.buildDirectory)
}