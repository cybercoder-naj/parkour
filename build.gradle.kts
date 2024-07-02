import org.jetbrains.dokka.gradle.DokkaTaskPartial
import java.net.URI
import java.util.*

plugins {
  kotlin("jvm") version "2.0.0"
  alias(libs.plugins.dokka)
  alias(libs.plugins.maven.publishing) apply false
}

group = "io.github.cybercoder-naj"
version = libs.versions.lib.get()

extra["docsDir"] = layout.projectDirectory.dir("docs/")
val docsDir = extra["docsDir"] as Directory

allprojects {
  repositories {
    mavenCentral()
  }
}

subprojects {
  extra["docsDir"] = rootProject.extra["docsDir"]

  apply(plugin = "org.jetbrains.dokka")
  tasks.withType<DokkaTaskPartial>().configureEach {
    dokkaSourceSets.configureEach {
      sourceLink {
        localDirectory.set(projectDir.resolve("src"))
        remoteUrl.set(URI.create("https://github.com/cybercoder-naj/parkour/tree/main/${project.name}/src").toURL())
        remoteLineSuffix.set("#L")
      }
    }
  }
}

tasks.dokkaHtmlMultiModule {
  moduleName.set("Parkour")
  outputDirectory.set(docsDir)
  moduleVersion.set(project.version.toString())
}

tasks.clean {
  delete = setOf(docsDir, layout.buildDirectory)
}