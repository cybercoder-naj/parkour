plugins {
    kotlin("jvm") version "1.9.23"
    application
}

group = "dev.cybercoder_nishant"
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
    mainClass = "dev.cybercoder_nishant.MainKt"
}