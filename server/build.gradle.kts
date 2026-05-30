
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
    kotlin("plugin.serialization") version "2.3.20"
}


application {
    mainClass = "io.ktor.server.cio.EngineMain"
}

kotlin {
    jvmToolchain(21)
}
dependencies {
    implementation(ktorLibs.server.cio)
    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.server.core)
    implementation(libs.logback.classic)


    implementation("io.ktor:ktor-serialization-kotlinx-json")


    implementation("io.ktor:ktor-server-cors")

    implementation("org.postgresql:postgresql:42.7.4")


    implementation("org.jetbrains.exposed:exposed-core:0.56.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.56.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.56.0")

    implementation("com.google.firebase:firebase-admin:9.2.0")
    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
}
