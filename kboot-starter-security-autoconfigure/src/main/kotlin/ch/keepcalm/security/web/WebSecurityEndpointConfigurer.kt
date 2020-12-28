package ch.keepcalm.security.web

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties

@EnableConfigurationProperties(WebSecurityEndpointConfigurer::class)
@ConfigurationProperties("keepcalm.security.endpoints")
@ConstructorBinding
class WebSecurityEndpointConfigurer(var admin: List<String> = listOf("/admin**"), var user: List<String> = listOf("/api**"), var unsecured: List<String> = listOf("/public**"))
