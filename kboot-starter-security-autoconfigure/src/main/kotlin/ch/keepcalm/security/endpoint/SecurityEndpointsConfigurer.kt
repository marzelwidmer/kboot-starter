package ch.keepcalm.security.endpoint

import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties

@ConstructorBinding
@EnableConfigurationProperties(SecurityEndpointsConfigurer::class)
@ConfigurationProperties("keepcalm.security.endpoints")
data class SecurityEndpointsConfigurer(val admin: List<String> = listOf("/admin**"), val user: List<String> = listOf("/api**"), val unsecured: List<String> = listOf("/public**")) {

    private val log = LoggerFactory.getLogger(javaClass)

    var userEndPoints: Array<String> = user.toTypedArray()
        .also {
            log.debug("Configure (User Endpoints) with [${it.joinToString()}]")
        }

    var adminEndPoints: Array<String> = admin.toTypedArray()
        .also {
            log.debug("Configure (Admin Endpoints) with [${it.joinToString()}]")
        }

    var unsecureEndPoints: Array<String> = unsecured.toTypedArray()
        .also {
            log.debug("Configure (Unsecured Endpoints) with [${it.joinToString()}]")
        }
}
