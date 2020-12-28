description = "Spring Boot Starter"

plugins {
    base
    id("idea")
    id("org.springframework.boot") version "2.4.1" apply false
    id("io.spring.dependency-management") version "1.0.10.RELEASE" apply false
    kotlin("jvm") version "1.4.21" apply false
    kotlin("plugin.spring") version "1.4.21" apply false
    id("org.sonarqube") version "3.0"
    `java-library`
    `maven-publish`
    jacoco
}

fun Project.envConfig() = object : kotlin.properties.ReadOnlyProperty<Any?, String?> {
    override fun getValue(thisRef: Any?, property: kotlin.reflect.KProperty<*>): String? =
        if (extensions.extraProperties.has(property.name)) {
            extensions.extraProperties[property.name] as? String
        } else {
            System.getenv(property.name)
        }
}

val version: String by extra
val group: String by extra
val projectDescription: String by extra
val projectName: String by extra
val projectRepository: String by extra
val developerId: String by extra
val developerName: String by extra
val developerEmail: String by extra

subprojects {
    sonarqube

    group = group
    version = version

    repositories {
        jcenter()
    }


    println("Enabling Java plugin in project ${project.name}...")
    apply(plugin = "java")

    println("Enabling JaCoCo plugin in project ${project.name}...")
    apply(plugin = "jacoco")

    println("Enabling maven-publish plugin in project ${project.name}...")
    apply(plugin = "maven-publish")

    println("Enabling Kotlin Spring plugin in project ${project.name}...")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")

    println("Enabling Kotlin  Spring plugin in project ${project.name}...")
    apply(plugin = "kotlin-spring")

    println("Enabling Spring Dependency Management plugin in project ${project.name}...")
    apply(plugin = "io.spring.dependency-management")

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

    tasks.test {
        finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
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
            println("--------> Publications ${project.name}")
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
