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
