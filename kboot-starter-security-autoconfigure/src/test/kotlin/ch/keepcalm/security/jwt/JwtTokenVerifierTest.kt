package ch.keepcalm.security.jwt

import ch.keepcalm.security.CustomUserDetails
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class JwtTokenVerifierTest {

    companion object {
        private const val JWT_TOKEN_LANGUAGE_EN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJhYWNmMWUyOS04ODY1LTRhNjgtOTcwMi03NTZkMWIyZjU3NjMiLCJzdWIiOiJqb2huLmRvZUBjaC5rZWVwY2FsbS5mb28uY2gua2VlcGNhbG0uYmFyLmNoIiwiaWF0IjoxNjA4NTgyNTcyLCJleHAiOjE2MTIxODI1NzI5OTksImlzcyI6IktlZXBjYWxtIEF1dGgiLCJhdWQiOiJLZWVwY2FsbSIsImxhbmd1YWdlIjoiZW4iLCJuYW1lIjoiRG9lIiwiZmlyc3ROYW1lIjoiSm9obiIsImVtYWlsIjoiam9oLmRvZUBjaC5rZWVwY2FsbS5mb28uY2gua2VlcGNhbG0uYmFyLmNoIiwicm9sZXMiOiJrZWVwY2FsbS51c2VyIn0.5EuWcv5wfSG3M4lEoZ9WglQEeZUvEEcHL_FwPh-QrTc"
        private const val JWT_TOKEN_LANGUAGE_DE = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJhYWNmMWUyOS04ODY1LTRhNjgtOTcwMi03NTZkMWIyZjU3NjMiLCJzdWIiOiJqb2huLmRvZUBjaC5rZWVwY2FsbS5mb28uY2gua2VlcGNhbG0uYmFyLmNoIiwiaWF0IjoxNjA4NTgyNTcyLCJleHAiOjE2MTIxODI1NzI5OTksImlzcyI6IktlZXBjYWxtIEF1dGgiLCJhdWQiOiJLZWVwY2FsbSIsImxhbmd1YWdlIjoiZGUiLCJuYW1lIjoiRG9lIiwiZmlyc3ROYW1lIjoiSm9obiIsImVtYWlsIjoiam9oLmRvZUBjaC5rZWVwY2FsbS5mb28uY2gua2VlcGNhbG0uYmFyLmNoIiwicm9sZXMiOiJrZWVwY2FsbS51c2VyIn0.Y9ap6YABYQcZPiQd0yUCu4q5GrMwMd_dPSTloqkm-a0"
        private const val JWT_TOKEN_LANGUAGE_IS_MISSING = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJhYWNmMWUyOS04ODY1LTRhNjgtOTcwMi03NTZkMWIyZjU3NjMiLCJzdWIiOiJqb2huLmRvZUBjaC5rZWVwY2FsbS5mb28uY2gua2VlcGNhbG0uYmFyLmNoIiwiaWF0IjoxNjA4NTgyNTcyLCJleHAiOjE2MTIxODI1NzI5OTksImlzcyI6IktlZXBjYWxtIEF1dGgiLCJhdWQiOiJLZWVwY2FsbSIsIm5hbWUiOiJEb2UiLCJmaXJzdE5hbWUiOiJKb2huIiwiZW1haWwiOiJqb2guZG9lQGNoLmtlZXBjYWxtLmZvby5jaC5rZWVwY2FsbS5iYXIuY2giLCJyb2xlcyI6ImtlZXBjYWxtLnVzZXIifQ.3epqCd60AxY6BtcvnzQTqmU2RA--kESfUinF9AbmUMc"
        private const val JWT_TOKEN_SECRET = "s3cretP@ssw0rd"
    }

    private var jwtTokenVerifier: JwtTokenVerifier? = null

    @BeforeAll
    fun setup() {
        val jwtProperties = JwtSecurityConfigurer()
        jwtProperties.secret = JWT_TOKEN_SECRET
        jwtTokenVerifier = JwtTokenVerifier(jwtProperties)
    }

    @Test
    fun `JWT language should be en`() {
        val authentication = jwtTokenVerifier?.getAuthentication(JWT_TOKEN_LANGUAGE_EN)
        val details = authentication?.principal as CustomUserDetails
        assertThat(details.language).isEqualTo("en")
    }

    @Test
    fun `JWT language should be de`() {
        val authentication = jwtTokenVerifier?.getAuthentication(JWT_TOKEN_LANGUAGE_DE)
        val details = authentication?.principal as CustomUserDetails
        assertThat(details.language).isEqualTo("de")
    }

    @Test
    fun `JWT language is Missing should be default value de`() {
        val authentication = jwtTokenVerifier?.getAuthentication(JWT_TOKEN_LANGUAGE_IS_MISSING)
        val details = authentication?.principal as CustomUserDetails
        assertThat(details.language).isEqualTo("de")
    }
}
