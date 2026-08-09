plugins {
    java
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21

    withSourcesJar()
    withJavadocJar()
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/") {
            name = "papermc"
        }
    }

    project.group = "com.github.ityeri.comshop"
    project.version = "v2.1.0"
}

subprojects {
    if (name == "example-plugin") return@subprojects
    if (name == "comshop-impl") return@subprojects

    plugins.apply("maven-publish")

    afterEvaluate {
        configure<PublishingExtension> {
            publications {
                create<MavenPublication>("mavenJava") {
                    components.findByName("java")?.let {
                        from(it)
                    }

                    artifactId = if (path.startsWith(":comshop-impl")) {
                        val mcVersionName = path.split(":").last()
                        "impl-$mcVersionName"
                    } else {
                        path.removePrefix(":comshop-")
                    }
                }
            }
        }
    }
}
