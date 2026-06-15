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
    project.version = "v1.0.0-beta.3"
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

                    if (path.startsWith(":comshop-impl")) {
                        val mcVersionName = path.split(":").last()
                        artifactId = "comshop-impl-$mcVersionName"
                    }
                }
            }
        }
    }
}
