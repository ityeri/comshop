plugins {
    alias(libs.plugins.kotlinPluginSerialization)
    kotlin("jvm")
}

subprojects {
    apply(plugin = "java-library")
    dependencies {
        api(project(":comshop-interface"))
    }
}