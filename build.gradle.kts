import org.jetbrains.dokka.gradle.DokkaTask

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
    outputDirectory.set(docsDir)
}

tasks.clean {
    delete = setOf(docsDir, layout.buildDirectory)
}