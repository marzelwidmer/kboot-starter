description = "Spring Boot Starter - Security Test Support - kboot-starter-security-test"

plugins {
    java
    kotlin("jvm")
    kotlin("plugin.spring")
}

val springBootVersion: String by extra
val springCloudVersion: String by extra
val springSecurityTest: String by extra

dependencies {
    // This dependency is exported to consumers, that is to say found on their compile classpath.
    api(project(":kboot-starter-security-autoconfigure"))

    // Kotlin dependencies
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    // Spring Boot dependencies
    implementation("org.springframework.boot:spring-boot-starter-security:$springBootVersion")
    implementation("org.springframework.security:spring-security-test:$springSecurityTest")

    // Test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
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
                name.set(System.getProperty("projectName"))
                description.set(System.getProperty("projectDescription"))
                url.set(System.getProperty("projectRepository"))

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set(System.getProperty("developerId"))
                        name.set(System.getProperty("developerName"))
                        email.set(System.getProperty("developerEmail"))
                    }
                }
            }
        }
    }
}
