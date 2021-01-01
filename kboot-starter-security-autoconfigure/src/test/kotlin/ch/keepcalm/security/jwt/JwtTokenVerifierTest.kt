package ch.keepcalm.security.jwt

import ch.keepcalm.security.user.CustomUserDetails
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class JwtTokenVerifierTest {

    companion object {
        private const val JWT_TOKEN_LANGUAGE_EN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJhZjM1ZjY0ZS1hMjFjLTQxNmUtODE4OC1iMjUxZjZhYmY1MGIiLCJzdWIiOiJqb2huLmRvZUBrZWVwY2FsbS5jaCIsImlhdCI6MTYwOTIyNjM3OSwiZXhwIjoxNjA5NTcxMjcyOTksImlzcyI6IktlZXBjYWxtIEF1dGggVGVzdCIsImF1ZCI6IktlZXBjYWxtIFRlc3QiLCJsYW5ndWFnZSI6ImVuIiwibGFzdG5hbWUiOiJEb2UiLCJmaXJzdG5hbWUiOiJKb2huIiwiZW1haWwiOiJqb2guZG9lQGtlZXBjYWxtLmNoIiwicm9sZXMiOiJrZWVwY2FsbS51c2VyIn0.yjch1wvNM_3Ix5JbPlfIiwW2qIJ9Vxw5WF8bV7DA9oU"
        private const val JWT_TOKEN_LANGUAGE_DE = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJhZjM1ZjY0ZS1hMjFjLTQxNmUtODE4OC1iMjUxZjZhYmY1MGIiLCJzdWIiOiJqb2huLmRvZUBrZWVwY2FsbS5jaCIsImlhdCI6MTYwOTIyNjM3OSwiZXhwIjoxNjA5NTcxMjcyOTksImlzcyI6IktlZXBjYWxtIEF1dGggVGVzdCIsImF1ZCI6IktlZXBjYWxtIFRlc3QiLCJsYW5ndWFnZSI6ImRlIiwibGFzdG5hbWUiOiJEb2UiLCJmaXJzdG5hbWUiOiJKb2huIiwiZW1haWwiOiJqb2guZG9lQGtlZXBjYWxtLmNoIiwicm9sZXMiOiJrZWVwY2FsbS51c2VyIn0.pzB7eJRi-MvAZGx3pDC5z79nug2ZiDGrRElf8C7rjlo"
        private const val JWT_TOKEN_LANGUAGE_IS_MISSING = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJhZjM1ZjY0ZS1hMjFjLTQxNmUtODE4OC1iMjUxZjZhYmY1MGIiLCJzdWIiOiJqb2huLmRvZUBrZWVwY2FsbS5jaCIsImlhdCI6MTYwOTIyNjM3OSwiZXhwIjoyNjA5MjIyNzc4OTksImlzcyI6IktlZXBjYWxtIEF1dGggVGVzdCIsImF1ZCI6IktlZXBjYWxtIFRlc3QiLCJsYXN0bmFtZSI6IkRvZSIsImZpcnN0bmFtZSI6IkpvaG4iLCJlbWFpbCI6ImpvaC5kb2VAa2VlcGNhbG0uY2giLCJyb2xlcyI6ImtlZXBjYWxtLnVzZXIifQ.UrkdoN5_ZEoELzxa0QSzYyse4odav6J1Bd9E_T42-Bg"
        private const val JWT_TOKEN_SECRET = "s3cretP@ssw0rd"
    }

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
