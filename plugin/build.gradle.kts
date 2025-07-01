plugins {
    kotlin("jvm") version "2.2.0"
    id("com.gradleup.shadow") version "8.3.0"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc"
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")

    implementation(project(":core"))
}

tasks.build {
    dependsOn("shadowJar")
}

kotlin {
    jvmToolchain(22)
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