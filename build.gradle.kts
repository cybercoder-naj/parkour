import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.dokka.gradle.DokkaTask
import java.net.URI
import java.util.*

plugins {
  kotlin("jvm") version "2.0.0"
  alias(libs.plugins.dokka)
  alias(libs.plugins.maven.publishing)
  `java-library`
  signing
}

group = "io.github.cybercoder-naj"
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
  jvmToolchain(17)
}

tasks.withType<DokkaTask>().configureEach {
  val projectName = project.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
  moduleName.set(projectName)
  moduleVersion.set(project.version.toString())
  outputDirectory.set(docsDir)
  dokkaSourceSets.configureEach {
    sourceLink {
      localDirectory.set(projectDir.resolve("src"))
      remoteUrl.set(URI.create("https://github.com/cybercoder-naj/parkour/tree/main/src").toURL())
      remoteLineSuffix.set("#L")
    }
  }
}

tasks.clean {
  delete = setOf(docsDir, layout.buildDirectory)
}

tasks.register<Jar>("dokkaHtmlJar") {
  dependsOn(tasks.dokkaHtml)
  from(tasks.dokkaHtml.flatMap { it.outputDirectory })
  archiveClassifier.set("html-docs")
}

mavenPublishing {
  configure(KotlinJvm(
    javadocJar = JavadocJar.Dokka("dokkaHtml"),
    sourcesJar = true
  ))

  publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, automaticRelease = true)

  signAllPublications()

  coordinates(project.group.toString(), project.name, project.version.toString())

  pom {
    name.set("parkour")
    description.set("Parser Combinator library for Kotlin")
    inceptionYear.set("2024")
    url.set("https://github.com/cybercoder-naj/parkour/")
    licenses {
      license {
        name.set("MIT License")
        url.set("https://github.com/cybercoder-naj/parkour/tree/main/LICENSE")
        distribution.set("repo")
      }
    }
    developers {
      developer {
        id.set("cybercoder-naj")
        name.set("Nishant Aanjaney Jalan")
        url.set("https://github.com/cybercoder-naj/")
      }
    }
    scm {
      url.set("https://github.com/cybercoder-naj/parkour/")
      connection.set("scm:git:git://github.com/cybercoder-naj/parkour.git")
      developerConnection.set("scm:git:ssh://git@github.com/cybercoder-naj/parkour.git")
    }
  }
}