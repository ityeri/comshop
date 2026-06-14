plugins {
    alias(libs.plugins.kotlinPluginSerialization)
    kotlin("jvm")
}

dependencies {
    api(libs.paperGlobal)
    compileOnly(libs.paperGlobal)
}
