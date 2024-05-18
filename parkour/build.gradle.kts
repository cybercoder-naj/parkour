plugins {
    kotlin("jvm") version "1.9.23"
    `java-library`
}

group = "dev.cybercoder_nishant"
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