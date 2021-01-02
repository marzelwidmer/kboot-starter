package ch.keepcalm.security.endpoint

import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.Test

class SecurityEndpointConfigurerTests {

    @Test
    fun `should give back the default values for EndpointConfiguration`() {
        val securityEndpointsConfigurer = SecurityEndpointsConfigurer()
        securityEndpointsConfigurer.user shouldContainAll listOf<String>("/api**")
        securityEndpointsConfigurer.admin shouldContainSame listOf<String>("/admin**")
        securityEndpointsConfigurer.unsecured shouldContainSame listOf<String>("/public**")
    }

    @Test
    fun `should give a configured unsecure Endpoint and the the default values for EndpointConfiguration`() {
        val securityEndpointsConfigurer = SecurityEndpointsConfigurer(unsecured = listOf("/unsecure"))
        securityEndpointsConfigurer.user shouldContainAll listOf<String>("/api**")
        securityEndpointsConfigurer.admin shouldContainSame listOf<String>("/admin**")
        securityEndpointsConfigurer.unsecured shouldContainSame listOf<String>("/unsecure")
    }

    @Test
    fun `should give a configured admin Endpoint and the the default values for EndpointConfiguration`() {
        val securityEndpointsConfigurer = SecurityEndpointsConfigurer(admin = listOf("/api/admin/**"))
        securityEndpointsConfigurer.user shouldContainAll listOf<String>("/api**")
        securityEndpointsConfigurer.admin shouldContainSame listOf("/api/admin/**")
        securityEndpointsConfigurer.unsecured shouldContainSame listOf<String>("/public**")
    }

    @Test
    fun `should give a configured user Endpoint and the the default values for EndpointConfiguration`() {
        val securityEndpointsConfigurer = SecurityEndpointsConfigurer(user = listOf("/api/user/foo/**", "/api/user/bar/**"))
        securityEndpointsConfigurer.user shouldContainAll listOf("/api/user/foo/**", "/api/user/bar/**")
        securityEndpointsConfigurer.admin shouldContainSame listOf<String>("/admin**")
        securityEndpointsConfigurer.unsecured shouldContainSame listOf<String>("/public**")
    }
}
