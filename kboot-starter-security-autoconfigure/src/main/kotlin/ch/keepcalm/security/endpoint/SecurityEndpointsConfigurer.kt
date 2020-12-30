package ch.keepcalm.security.endpoint

import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties

@EnableConfigurationProperties(SecurityEndpointsConfigurer::class)
@ConfigurationProperties("keepcalm.security.endpoints")
class SecurityEndpointsConfigurer(var admin: List<String> = listOf("/admin**"), var user: List<String> = listOf("/api**"), var unsecured: List<String> = listOf("/public**")) {


    private val log = LoggerFactory.getLogger(javaClass)

    fun getUserEndpoints() = user.toTypedArray()
        .also {
            log.debug("Configure (User Endpoints) with [${it.joinToString()}]")
        }

    fun getAdminEndpoints() = admin.toTypedArray()
        .also {
            log.debug("Configure (Admin Endpoints) with [${it.joinToString()}]")
        }

    fun getUnsecuredEndpoints() = unsecured.toTypedArray()
        .also {
            log.debug("Configure (Unsecured Endpoints) with [${it.joinToString()}]")
        }
}
