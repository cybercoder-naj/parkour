import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
  kotlin("jvm")
  alias(libs.plugins.dokka)
  alias(libs.plugins.maven.publishing)
  `java-library`
  signing
}

group = "io.github.cybercoder-naj"
version = "0.1.0"

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

tasks.withType<DokkaTaskPartial>().configureEach {
  dokkaSourceSets.configureEach {
    moduleName.set("parkour")
  }
}

tasks.register<Jar>("dokkaHtmlJar") {
  dependsOn(tasks.dokkaHtml)
  from(tasks.dokkaHtml.flatMap { it.outputDirectory })
  archiveClassifier.set("html-docs")
}

mavenPublishing {
  configure(
    KotlinJvm(
      javadocJar = JavadocJar.Dokka("dokkaHtml"),
      sourcesJar = true
    )
  )

  publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, automaticRelease = true)

  signAllPublications()

  coordinates(project.group.toString(), "parkour", project.version.toString())

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
