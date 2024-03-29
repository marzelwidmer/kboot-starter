description = "Spring Boot Starter - kboot-starter-autoconfigure"

plugins {
    java
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("kapt")
    // TODO: 10.04.23  disable contracts
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
    api("io.jsonwebtoken", "jjwt-api", jjwtVersion)
    runtimeOnly("io.jsonwebtoken", "jjwt-impl", jjwtVersion)
    runtimeOnly("io.jsonwebtoken", "jjwt-jackson", jjwtVersion)

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
    testImplementation("org.springframework.boot", "spring-boot-starter-test", springBootVersion)

    testImplementation("io.projectreactor", "reactor-test", reactorTestVersion)
    testImplementation("org.amshove.kluent", "kluent", kluentVersion)

    // Spring Cloud Contracts
    testImplementation("org.springframework.cloud", "spring-cloud-starter-contract-verifier")
    testImplementation("org.springframework.cloud", "spring-cloud-contract-spec-kotlin")
    testImplementation("org.springframework.cloud", "spring-cloud-starter-contract-stub-runner")
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
            // TODO: 10.04.23  disable contracts
            artifact(tasks.named("verifierStubsJar"))

            // https://github.com/spring-gradle-plugins/dependency-management-plugin/issues/273
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
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
