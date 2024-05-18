plugins {
    kotlin("jvm") version "1.9.23"
    application
}

group = "io.github.cybercodernaj"
version = libs.versions.lib.get()

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":parkour"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(18)
}

application {
    mainClass = "io.github.cybercodernaj.MainKt"
}