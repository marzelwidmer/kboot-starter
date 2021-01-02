description = "Spring Boot Starter - kboot-starter-autoconfigure"

plugins {
    java
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("kapt")
    id("org.springframework.cloud.contract")
}

val springBootVersion: String by extra
val springSecurityTest: String by extra
val springCloudVersion: String by extra
val jjwtVersion: String by extra
val jacksonModuleKotlinVersion: String by extra
val kluentVersion: String by extra
val kotlinVersion: String by extra
val kotlinExtensionsVersion: String by extra
val kotlinxCoroutinesReactorVersion: String by extra
val reactorTestVersion: String by extra

// import Spring Cloud  BOM
dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
    }
}
contracts {
    testFramework.set(org.springframework.cloud.contract.verifier.config.TestFramework.JUNIT5)
    packageWithBaseClasses.set("ch.keepcalm.security.mock.base")
}

dependencies {
    // This dependency is exported to consumers, that is to say found on their compile classpath.
    api("io.jsonwebtoken", "jjwt", jjwtVersion)

    // Kotlin dependencies
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-reactor", kotlinxCoroutinesReactorVersion)

    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin", jacksonModuleKotlinVersion)
    implementation("io.projectreactor.kotlin", "reactor-kotlin-extensions", kotlinExtensionsVersion)

    // Spring Boot dependencies
    implementation("org.springframework.boot", "spring-boot-starter-security", springBootVersion)
    implementation("org.springframework.boot", "spring-boot-starter-web", springBootVersion)
    implementation("org.springframework.boot", "spring-boot-starter-webflux", springBootVersion)
    implementation("org.springframework.boot", "spring-boot-starter-actuator", springBootVersion)
    // Annotation Processors
    annotationProcessor("org.springframework.boot", "spring-boot-autoconfigure-processor", springBootVersion)
    annotationProcessor("org.springframework.boot", "spring-boot-configuration-processor", springBootVersion)

    // Test dependencies
    testImplementation("org.springframework.security", "spring-security-test", springSecurityTest)
    testImplementation("org.springframework.boot", "spring-boot-starter-test", springBootVersion) {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(group = "junit", module = "junit")
    }

    testImplementation("io.projectreactor", "reactor-test", reactorTestVersion)
    testImplementation(kotlin("test"))
    testImplementation("org.amshove.kluent", "kluent", kluentVersion)

    // Spring Cloud Contracts
    testImplementation("org.springframework.cloud", "spring-cloud-starter-contract-verifier")
    testImplementation("org.springframework.cloud", "spring-cloud-contract-spec-kotlin")
    testImplementation("org.springframework.cloud", "spring-cloud-starter-contract-stub-runner")
}


publishing {
    publications {
        create<MavenPublication>("mavenJava") {

            // remove::start[]
            artifact(tasks.named("verifierStubsJar"))
            // remove::end[]

            // https://github.com/spring-gradle-plugins/dependency-management-plugin/issues/273
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
        }
    }
}
