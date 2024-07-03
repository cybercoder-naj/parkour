import org.jetbrains.dokka.gradle.DokkaMultiModuleTask
import org.jetbrains.dokka.gradle.DokkaTaskPartial
import java.net.URI

plugins {
  kotlin("jvm") version "2.0.0"
  alias(libs.plugins.dokka)
  alias(libs.plugins.maven.publishing) apply false
}

group = "io.github.cybercoder-naj"
version = libs.versions.lib.get()

extra["docsDir"] = layout.projectDirectory.dir("docs/")
val docsDir = extra["docsDir"] as Directory

buildscript {
  dependencies {
    classpath(libs.dokka.base)
  }
}

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

tasks.withType<DokkaMultiModuleTask>().configureEach {
  val config = """
    {
      "footerMessage": "© 2024 Nishant Aanjaney Jalan"
    } 
  """
  pluginsMapConfiguration.set(
    mapOf(
      "org.jetbrains.dokka.base.DokkaBase" to config
    )
  )
}

tasks.clean {
  delete = setOf(docsDir, layout.buildDirectory)
}