plugins {
    kotlin("jvm") version "2.2.0"
    `maven-publish`
}

group = "io.github.ityeri"
version = "0.0.0"

publishing {
    publications {
        create<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/yourusername/your-repo")
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

    compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")
}

kotlin {
    jvmToolchain(22)
}
