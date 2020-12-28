package ch.keepcalm.security.jwt

import ch.keepcalm.security.CustomUserDetails
import io.jsonwebtoken.*
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken

class JwtTokenVerifier(private val jwtSecurityConfigurer: JwtSecurityConfigurer) {
    companion object {
        private val LOG = LoggerFactory.getLogger(JwtTokenVerifier::class.java)
        private const val AUTHORITIES_KEY = "roles"
        private const val FIRST_NAME = "firstname"
        private const val LAST_NAME = "lastname"
        private const val LANGUAGE = "language"
    }

    private object DEFAULTS {
        const val DEFAULT_LANGUAGE = "de"
    }

    fun getAuthentication(token: String): Authentication =
            createAuthenticationToken(createPrincipal(toClaims(token)), token)

    private fun toClaims(token: String): Claims =
            Jwts.parser()
                    .setSigningKey(jwtSecurityConfigurer.secret)
                    .parseClaimsJws(token)
                    .body

    private fun createAuthenticationToken(principal: UserDetails, token: String) =
            PreAuthenticatedAuthenticationToken(principal, token, principal.authorities)

    private fun createPrincipal(claims: Claims): UserDetails {
        val language = extractLanguage(claims)
        return CustomUserDetails(subject = claims.subject,
                                 password = "",
                                 firstname = "${claims[FIRST_NAME]}",
                                 lastname = "${claims[LAST_NAME]}",
                                 authorities = commaSeparatedStringToAuthorityList(
                                         claims.get(
                                             AUTHORITIES_KEY,
                                                    String::class.java)),
                                 language = language)
    }

    private fun extractLanguage(claims: Claims): String {
        var language = claims.get(LANGUAGE, String::class.java)
        if (language.isNullOrBlank()) {
            language = DEFAULTS.DEFAULT_LANGUAGE
        }
        return language
    }

    fun validateToken(authToken: String): Boolean {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecurityConfigurer.secret)
                    .requireIssuer(jwtSecurityConfigurer.issuer)
                    .requireAudience(jwtSecurityConfigurer.audience)
                    .parseClaimsJws(authToken)
            return true
        } catch (e: SignatureException) {
            LOG.info("Invalid JWT signature.")
            LOG.trace("Invalid JWT signature trace: {}", e)
        } catch (e: MalformedJwtException) {
            LOG.info("Invalid JWT token.")
            LOG.trace("Invalid JWT token trace: {}", e)
        } catch (e: ExpiredJwtException) {
            LOG.info("Expired JWT token.")
            LOG.trace("Expired JWT token trace: {}", e)
        } catch (e: UnsupportedJwtException) {
            LOG.info("Unsupported JWT token.")
            LOG.trace("Unsupported JWT token trace: {}", e)
        } catch (e: IllegalArgumentException) {
            LOG.info("JWT token compact of handler are invalid.")
            LOG.trace("JWT token compact of handler are invalid trace: {}", e)
        }

        return false
    }
}
