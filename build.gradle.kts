plugins {
    kotlin("jvm") version "2.2.0"
    `maven-publish`
}

repositories {
    mavenCentral()
}

allprojects {
    group = "com.github.ityeri.comshop"
    version = "v1.0.0-beta"
}

val excludeModules = listOf("comshop-plugin")

subprojects {
    if (project.name in excludeModules) { return@subprojects }

    apply(plugin = "java")
    apply(plugin = "maven-publish")

    java {
        withSourcesJar()
        withJavadocJar()
    }

    publishing {
        publications {
            create<MavenPublication>("jitpack") {
                groupId = project.group as String
                artifactId = project.name
                version = project.version as String
                from(components["java"])
            }
        }
    }
}