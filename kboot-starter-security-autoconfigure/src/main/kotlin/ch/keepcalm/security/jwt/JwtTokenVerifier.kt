package ch.keepcalm.security.jwt

import ch.keepcalm.security.user.CustomUser
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken

class JwtTokenVerifier(private val securityJwtConfigurer: SecurityJwtConfigurer) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun getAuthentication(token: String): Authentication {
        return createPreAuthenticatedAuthenticationToken(CustomUser().createPrincipal(toClaims(token)), token)
    }

    private fun createPreAuthenticatedAuthenticationToken(principal: UserDetails, token: String): PreAuthenticatedAuthenticationToken =
        PreAuthenticatedAuthenticationToken(principal, token, principal.authorities)

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
    fun verify(token: String): Jws<Claims> = Jwts.parser()
        .setSigningKey(securityJwtConfigurer.secret)
        .requireAudience(securityJwtConfigurer.audience)
        .requireIssuer(securityJwtConfigurer.issuer)
        .parseClaimsJws(token)

    private fun toClaims(token: String): Claims =
        Jwts.parser()
            .setSigningKey(securityJwtConfigurer.secret)
            .parseClaimsJws(token)
            .body
}
