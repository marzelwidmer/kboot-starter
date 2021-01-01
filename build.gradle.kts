description = "Spring Boot Starter"

plugins {
    base
    id("idea")
    id("org.springframework.boot") version "2.4.1" apply false
    id("io.spring.dependency-management") version "1.0.10.RELEASE" apply false
    kotlin("jvm") version "1.4.21" apply false
    kotlin("plugin.spring") version "1.4.21" apply false

    id("org.jmailen.kotlinter") version "3.3.0"

    id("org.sonarqube") version "3.0"
    `java-library`
    `maven-publish`
    jacoco
}

val version: String by extra
val group: String by extra
val projectDescription: String by extra
val projectName: String by extra
val projectRepository: String by extra
val developerId: String by extra
val developerName: String by extra
val developerEmail: String by extra

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
}

buildscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("org.jmailen.gradle", "kotlinter-gradle", "3.3.0")
    }
}

fun Project.envConfig() = object : kotlin.properties.ReadOnlyProperty<Any?, String?> {
    override fun getValue(thisRef: Any?, property: kotlin.reflect.KProperty<*>): String? =
        if (extensions.extraProperties.has(property.name)) {
            extensions.extraProperties[property.name] as? String
        } else {
            System.getenv(property.name)
        }
}

subprojects {

    group = group
    version = version

    apply(plugin = "java")
    apply(plugin = "jacoco")
    apply(plugin = "org.jmailen.kotlinter")
    apply(plugin = "maven-publish")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "kotlin-spring")
    apply(plugin = "io.spring.dependency-management")

    kotlinter {
        ignoreFailures = true
        indentSize = 4
        reporters = arrayOf("checkstyle", "plain", "html")
        experimentalRules = false
        disabledRules = arrayOf("no-wildcard-imports")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            languageVersion = "1.4"
            apiVersion = "1.4"
            jvmTarget = "11"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    sonarqube

    tasks.jacocoTestReport {
        reports {
            xml.isEnabled = true
            csv.isEnabled = false
            html.destination = file("$buildDir/jacocoHtml")
        }
    }
    tasks.jacocoTestCoverageVerification {
        violationRules {
            rule {
                enabled = true
                element = "CLASS"
                excludes = listOf("com.jacoco.dto.*")
                limit {
                    counter = "BRANCH"
                    minimum = 0.0.toBigDecimal()
                }
            }
        }
    }
    tasks.test {
        // report is always generated after tests run
        finalizedBy(tasks.jacocoTestReport)
        finalizedBy(tasks.jacocoTestCoverageVerification)
    }

    publishing {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/marzelwidmer/kboot-starter")
                credentials {
                    username = System.getenv("GITHUB_ACTOR")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }
        }

        publications {
            create<MavenPublication>(project.name) {
                from(components["java"])

                pom {
                    name.set(projectName)
                    description.set(projectDescription)
                    url.set(projectRepository)

                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set(developerId)
                            name.set(developerName)
                            email.set(developerEmail)
                        }
                    }
                }
            }
        }
    }
}
