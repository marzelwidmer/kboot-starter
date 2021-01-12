description = "Spring Boot Starter - kboot-starter-security"

plugins {
    `java-library`
}
dependencies {
    api(project(":kboot-starter-security-autoconfigure"))
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
