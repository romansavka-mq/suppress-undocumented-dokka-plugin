import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

plugins {
    kotlin("jvm") version "1.7.10"
    id("org.jetbrains.dokka") version "1.7.10" // Used to create a javadoc jar
    `maven-publish`
    signing
}

group = "io.github.romansavka-mq"
version = "1.1"

repositories {
    mavenCentral()
}

val dokkaVersion: String by project
dependencies {
    implementation(kotlin("stdlib"))
    compileOnly("org.jetbrains.dokka:dokka-core:$dokkaVersion")
    implementation("org.jetbrains.dokka:dokka-base:$dokkaVersion")

    testImplementation(kotlin("test-junit"))
    testImplementation("org.jetbrains.dokka:dokka-test-api:$dokkaVersion")
    testImplementation("org.jetbrains.dokka:dokka-base-test-utils:$dokkaVersion")
}

val dokkaOutputDir = "$buildDir/dokka"

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
    dokkaHtml {
        outputDirectory.set(file(dokkaOutputDir))
    }
}

val javadocJar by tasks.registering(Jar::class) {
    dependsOn(tasks.dokkaHtml)
    archiveClassifier.set("javadoc")
    from(dokkaOutputDir)
}

java {
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = project.name
            from(components["java"])
            artifact(javadocJar.get())

            pom {
                name.set("Suppress Undocumented Dokka plugin")
                description.set("This is a plugin to generate docs only for documented members")
                url.set("https://github.com/romansavka-mq/suppress-undocumented-dokka-plugin")

                licenses {
                    license {
                        name.set("The Apache Software License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                }

                developers {
                    developer {
                        id.set("romansavka-mq")
                        name.set("Roman Savka")
                        url.set("https://github.com/romansavka-mq")
                    }
                }

                scm {
                    connection.set("scm:git:git@github.com:romansavka-mq/suppress-undocumented-dokka-plugin.git")
                    url.set("https://github.com/romansavka-mq/suppress-undocumented-dokka-plugin/tree/main")
                }
            }
        }
    }

    repositories {
        maven {
            url = URI("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = findProperty("sonatype.user") as String?
                password = findProperty("sonatype.password") as String?
            }
        }
    }
}

signing {
    // Credentials should be configured according to
    // https://docs.gradle.org/current/userguide/signing_plugin.html#sec:signatory_credentials
    sign(publishing.publications.getByName("mavenJava"))
}