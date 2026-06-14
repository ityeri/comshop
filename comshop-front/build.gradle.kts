plugins {
    alias(libs.plugins.kotlinPluginSerialization)
    kotlin("jvm")
}

dependencies {
    api(project(":comshop-interface"))
}
