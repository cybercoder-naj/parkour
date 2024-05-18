plugins {
    kotlin("jvm")
    alias(libs.plugins.dokka)
    `java-library`
}

group = "io.github.cybercodernaj"
version = libs.versions.lib.get()

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