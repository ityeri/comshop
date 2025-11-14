plugins {
    kotlin("jvm") version "2.2.0"
    `maven-publish`
}

repositories {
    mavenCentral()
}

subprojects {
    project.group = "com.github.ityeri.comshop"
    project.version = "v1.0.0-beta"
}