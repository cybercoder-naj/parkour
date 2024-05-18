plugins {
    kotlin("jvm")
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