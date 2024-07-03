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

val docsDir = layout.projectDirectory.dir("docs/")
val dokkaPluginConfig = """
    {
      "footerMessage": "© 2024 Nishant Aanjaney Jalan"
    } 
  """

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
  apply(plugin = "org.jetbrains.dokka")
  tasks.withType<DokkaTaskPartial>().configureEach {
    dokkaSourceSets.configureEach {
      sourceLink {
        localDirectory.set(projectDir.resolve("src"))
        remoteUrl.set(URI.create("https://github.com/cybercoder-naj/parkour/tree/main/${project.name}/src").toURL())
        remoteLineSuffix.set("#L")
      }
    }

    pluginsMapConfiguration.set(
      mapOf(
        "org.jetbrains.dokka.base.DokkaBase" to dokkaPluginConfig
      )
    )
  }
}

tasks.dokkaHtmlMultiModule {
  moduleName.set("Parkour")
  outputDirectory.set(docsDir)
  moduleVersion.set(project.version.toString())
}

tasks.withType<DokkaMultiModuleTask>().configureEach {
  pluginsMapConfiguration.set(
    mapOf(
      "org.jetbrains.dokka.base.DokkaBase" to dokkaPluginConfig
    )
  )
}

tasks.clean {
  delete = setOf(docsDir, layout.buildDirectory)
}