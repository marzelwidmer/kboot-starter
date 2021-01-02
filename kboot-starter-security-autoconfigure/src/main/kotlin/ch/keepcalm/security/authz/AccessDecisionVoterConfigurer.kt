package ch.keepcalm.security.authz

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties

@ConstructorBinding
@EnableConfigurationProperties(AccessDecisionVoterConfigurer::class)
@ConfigurationProperties("keepcalm.security.access-decision-voter")
data class AccessDecisionVoterConfigurer(val voters: List<String> = listOf("OPA"), val url: String = "http://localhost:8181/v1/data/http/authz/allow")
