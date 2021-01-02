package ch.keepcalm.security.authz

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContainSame
import org.junit.jupiter.api.Test

class AccessDecisionVoterConfigurerTests {

    @Test
    fun `should give back the default values for AccessDecisionVoterConfigurer`() {
        val accessDecisionVoterConfigurer = AccessDecisionVoterConfigurer()
        accessDecisionVoterConfigurer.url shouldBeEqualTo "http://localhost:8181/v1/data/http/authz/allow"
        accessDecisionVoterConfigurer.voters shouldContainSame listOf("OPA")
    }
}
