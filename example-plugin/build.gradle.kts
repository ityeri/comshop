plugins {
    alias(libs.plugins.kotlinPluginSerialization)
    kotlin("jvm")
    id("com.gradleup.shadow") version "8.3.0"
    id("xyz.jpenilla.run-paper") version "3.0.2"
}

val pluginPaperVersion = "1.21.4"

dependencies {
//    compileOnly("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-api:$pluginPaperVersion-R0.1-SNAPSHOT")

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
    jvmToolchain(21)
}

tasks.jar {
    manifest {
        attributes["Implementation-Version"] = version
    }
    from(
        configurations.compileClasspath.get().filter {
            it.name.endsWith("kotlin-stdlib.jar")
        }.map {
            if (it.isDirectory) it else zipTree(it)
        }
    )

    // To avoid the duplicate handling strategy error
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // To add all of the dependencies
    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })

}
