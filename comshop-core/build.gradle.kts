plugins {
    kotlin("jvm") version "2.2.0"
    `maven-publish`
}

group = "io.github.ityeri"
version = "v1.0.0-beta"
base.archivesName.set("comshop-core")

publishing {
    publications {
        create<MavenPublication>("gpr") {
            artifactId = "comshop-core"
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/ityeri/comshop")
            credentials {
                username = findProperty("gpr.user") as String? ?: ""
                password = findProperty("gpr.key") as String? ?: ""
            }
        }
    }
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc"
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation(kotlin("stdlib-jdk8"))

    api("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")

    testImplementation("io.kotest:kotest-runner-junit5:5.8.1")
    testImplementation("io.kotest:kotest-assertions-core:5.8.1")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}