description = "Spring Boot Starter - kboot-starter-autoconfigure"

plugins {
    java
    kotlin("jvm")
}

val springBootVersion: String by extra
val springSecurityTest: String by extra
val jjwtVersion: String by extra
val jacksonModuleKotlin: String by extra


dependencies {
    // This dependency is exported to consumers, that is to say found on their compile classpath.
    api("io.jsonwebtoken", "jjwt", jjwtVersion)

    // Kotlin dependencies
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin", jacksonModuleKotlin)

    // Spring Boot dependencies
    implementation("org.springframework.boot", "spring-boot-starter-security", springBootVersion)
    compileOnly("org.springframework.boot", "spring-boot-starter-web", springBootVersion)
    compileOnly("org.springframework.boot", "spring-boot-starter-webflux", springBootVersion)
    implementation("org.springframework.boot", "spring-boot-starter-actuator", springBootVersion)
    // Annotation Processors
    annotationProcessor("org.springframework.boot", "spring-boot-autoconfigure-processor", springBootVersion)
    annotationProcessor("org.springframework.boot", "spring-boot-configuration-processor", springBootVersion)


    // Test dependencies
    testImplementation("org.springframework.security", "spring-security-test", springSecurityTest)
    testImplementation("org.springframework.boot", "spring-boot-starter-test", springBootVersion)
    testImplementation(kotlin("test"))
    testImplementation("org.amshove.kluent", "kluent", "1.64")

}
