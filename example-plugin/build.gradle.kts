plugins {
    alias(libs.plugins.kotlinPluginSerialization)
    kotlin("jvm")
    id("com.gradleup.shadow") version "8.3.0"
    id("xyz.jpenilla.run-paper") version "3.0.2"
}

val pluginPaperVersion = "26.2"

dependencies {
    compileOnly("io.papermc.paper:paper-api:$pluginPaperVersion.build.+")

    implementation(project(":comshop-impl:1.21.10"))
    implementation(project(":comshop-front"))
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.runServer {
    minecraftVersion(pluginPaperVersion)
}

kotlin {
    jvmToolchain(25)
}

