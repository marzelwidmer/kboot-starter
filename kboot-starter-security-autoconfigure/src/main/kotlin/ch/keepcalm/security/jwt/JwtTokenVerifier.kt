package ch.keepcalm.security.jwt

import ch.keepcalm.security.CustomUserDetails
import io.jsonwebtoken.*
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken

class JwtTokenVerifier(private val securityJwtConfigurer: SecurityJwtConfigurer) {

    private val log = LoggerFactory.getLogger(javaClass)

    companion object {
        private const val AUTHORITIES_KEY = "roles"
        private const val FIRST_NAME = "firstname"
        private const val LAST_NAME = "lastname"
        private const val LANGUAGE = "language"
    }

    private object DEFAULTS {
        const val DEFAULT_LANGUAGE = "de"
        const val DEFAULT_FIRSTNAME = "anonymous"
        const val DEFAULT_LASTNAME = "anonymous"
    }

    fun validateToken(token: String): Boolean {
        try {
            verify(token)
            return true
        } catch (e: JwtException) {
            log.info("There was an error validating the JWT")
            log.trace("There was an error validating the JWT trace: {}", e)
        }
        return false
    }

    /**
     * Verify JWT token.
     *
     * @param token String
     * @return Jws<Claims>
     */
    private fun verify(token: String): Jws<Claims> = Jwts.parser()
        .setSigningKey(securityJwtConfigurer.secret)
        .requireAudience(securityJwtConfigurer.audience)
        .requireIssuer(securityJwtConfigurer.issuer)
        .parseClaimsJws(token)


    /**
     * Extracts Claims from JWT.
     *
     * @param token String
     * @return (io.jsonwebtoken.Claims..io.jsonwebtoken.Claims?)
     */
    fun getClamsFromJwt(token: String) = verify(token).body

    /**
     * Get the Authenticated User
     */
    fun getAuthentication(token: String): Authentication {
        val principal = createCustomUserDetailsFromJwtClaims(getClamsFromJwt(token))
       return PreAuthenticatedAuthenticationToken(principal, token, principal.authorities)
    }



    private fun createCustomUserDetailsFromJwtClaims(claims: Claims) =
        CustomUserDetails(
            subject = claims.subject,
            password = "",
            firstname = extractFirstNameFromClaims(claims),
            lastname = extractLastNameFromClaims(claims),
            authorities = commaSeparatedStringToAuthorityList(
                claims.get(
                    AUTHORITIES_KEY,
                    String::class.java
                )
            ),
            language = extractLanguageFromClaims(claims)
        )
    private fun extractLanguageFromClaims(claims: Claims): String {
        var language = claims.get(LANGUAGE, String::class.java)
        if (language.isNullOrBlank()) {
            language = DEFAULTS.DEFAULT_LANGUAGE
        }
        return language
    }

    private fun extractFirstNameFromClaims(claims: Claims): String {
        var firstName = claims.get(FIRST_NAME, String::class.java)
        if (firstName.isNullOrBlank()) {
            firstName = DEFAULTS.DEFAULT_FIRSTNAME
        }
        return firstName
    }

    private fun extractLastNameFromClaims(claims: Claims): String {
        var lastName = claims.get(LAST_NAME, String::class.java)
        if (lastName.isNullOrBlank()) {
            lastName = DEFAULTS.DEFAULT_LASTNAME
        }
        return lastName
    }
}
