# KBOOT Spring Boot Starter
![Master Branch](https://github.com/marzelwidmer/kboot-starter/workflows/Master%20Branch/badge.svg) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=kboot-starter&metric=alert_status)](https://sonarcloud.io/dashboard?id=kboot-starter)
## Configure JWT
```yaml
keepcalm:
  security:
    jwt:
      issuer: Keepcalm Auth
      audience: Keepcalm
      secret: s3cretP@ssw0rd
```
## Configure Protected EndPoints
To override the default configuration of access of a User Token `/api` add the following configuration in you `application.yaml` file
and also if you configure a Admin Endpoints.
```yaml
keepcalm:
  security:
    endpoints:
      admin:
        - "/api/salary/**"
      user:
        - "/api/document/**"
      unsecured:
        - "/faketoken"
```

## Maven
```xml
<profile>
    <id>github</id>
    <distributionManagement>
        <repository>
            <id>github</id>
            <name>GitHub Apache Maven Packages</name>
            <url>https://maven.pkg.github.com/marzelwidmer/kboot-starter</url>
        </repository>
    </distributionManagement>
</profile>
```

```xml
<dependency>
	<groupId>ch.keepcalm.security</groupId>
	<artifactId>kboot-starter-security</artifactId>
	<version>VERSION</version>
</dependency>
	
```



# Test Support
```xml
<!-- Test Support -->
<dependency>
	<groupId>ch.keepcalm.security</groupId>
	<artifactId>kboot-starter-security-test</artifactId>
	<version>VERSION</version>
	<scope>test</scope>
</dependency>
```

```kotlin


@SpringBootTest
@SpringBootConfiguration
class WithMockCustomerTest {

	@WithMockCustomUser(username = "jane@doe.ch", authorities = ["keepcalm.user"], firstname = "jane", lastname = "doe")
	@Test
	fun `test SecurityContext of Jane Doe`() {
		assertThat(getPrincipalFirstName()).isEqualTo("jane")
		assertThat(getPrincipalLastName()).isEqualTo("doe")
		assertThat(getCurrentUsername()).isEqualTo("jane@doe.ch")
	}
}


```
