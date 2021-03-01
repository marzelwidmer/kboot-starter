package ch.keepcalm.security.jwt

import ch.keepcalm.security.JWT_TOKEN_LANGUAGE_DE
import ch.keepcalm.security.JWT_TOKEN_LANGUAGE_EN
import ch.keepcalm.security.JWT_TOKEN_LANGUAGE_IS_MISSING
import ch.keepcalm.security.JWT_TOKEN_SECRET
import ch.keepcalm.security.user.CustomUserDetails
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class JwtTokenVerifierTest {

    private var jwtTokenVerifier: JwtTokenVerifier? = null

    @BeforeAll
    fun setup() {
        val jwtProperties = SecurityJwtConfigurer()
        jwtProperties.secret = JWT_TOKEN_SECRET
        jwtTokenVerifier = JwtTokenVerifier(jwtProperties)
    }

    @Test
    fun `JWT language should be en`() {
        val authentication = jwtTokenVerifier?.getAuthentication(JWT_TOKEN_LANGUAGE_EN)
        val details = authentication?.principal as CustomUserDetails
        details.language shouldBeEqualTo "en"
    }

    @Test
    fun `JWT language should be de`() {
        val authentication = jwtTokenVerifier?.getAuthentication(JWT_TOKEN_LANGUAGE_DE)
        val details = authentication?.principal as CustomUserDetails
        details.language shouldBeEqualTo "de"
    }

    @Test
    fun `JWT language is Missing should be default value de`() {
        val authentication = jwtTokenVerifier?.getAuthentication(JWT_TOKEN_LANGUAGE_IS_MISSING)
        val details = authentication?.principal as CustomUserDetails
        details.language shouldBeEqualTo "de"
    }

    @Test
    fun `JWT firstname should be John`() {
        val authentication = jwtTokenVerifier?.getAuthentication(JWT_TOKEN_LANGUAGE_EN)
        val details = authentication?.principal as CustomUserDetails
        details.firstname shouldBeEqualTo "John"
    }

    @Test
    fun `JWT lastname should be Doe`() {
        val authentication = jwtTokenVerifier?.getAuthentication(JWT_TOKEN_LANGUAGE_EN)
        val details = authentication?.principal as CustomUserDetails
        details.lastname shouldBeEqualTo "Doe"
    }
}
