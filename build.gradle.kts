import kotlin.properties.ReadOnlyProperty

description = "Spring Boot Starter"

plugins {
    id("org.springframework.boot") version "2.7.10" apply false
    id("io.spring.dependency-management") version "1.0.15.RELEASE" apply false
    kotlin("jvm") version "1.8.20" apply false
    kotlin("plugin.spring") version "1.8.20" apply false
    base
    id("idea")
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

buildscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
            url = uri("https://repo.spring.io/snapshot")
            url = uri("https://repo.spring.io/milestone")
            url = uri("https://repo.spring.io/release")
        }
    }
    dependencies {
        classpath("org.jmailen.gradle", "kotlinter-gradle", "3.3.0")
        classpath("org.springframework.cloud", "spring-cloud-contract-gradle-plugin", "3.1.0")
        classpath("org.springframework.cloud", "spring-cloud-contract-spec-kotlin", "3.1.0")
    }
}

fun Project.envConfig() = ReadOnlyProperty<Any?, String?> { _, property ->
    if (extensions.extraProperties.has(property.name)) {
        extensions.extraProperties[property.name] as? String
    } else {
        System.getenv(property.name)
    }
}

subprojects {

    repositories {
        mavenLocal()
        mavenCentral()
        maven { url = uri("https://repo.spring.io/milestone") }
    }

    group = group
    version = version

    apply(plugin = "java")
    apply(plugin = "jacoco")
    apply(plugin = "org.jmailen.kotlinter")
    apply(plugin = "maven-publish")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    sonarqube

    tasks.jacocoTestReport {
        reports {
            xml.required.set(true)
            csv.required.set(false)
            html.outputLocation.set(file("$buildDir/jacocoHtml"))
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

    tasks.withType<Delete> {
        doFirst {
            delete("~/.m2/repository/ch/keepcalm/security/")
        }
    }
}
