plugins {
    alias(libs.plugins.kotlinPluginSerialization)
    kotlin("jvm")
}

dependencies {
    api("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")
}
