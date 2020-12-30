package ch.keepcalm.security.webflux

import ch.keepcalm.security.AUTHENTICATION_SCHEMA
import ch.keepcalm.security.jwt.JwtTokenVerifier
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

class JwtAuthenticationConverter(private val jwtTokenVerifier: JwtTokenVerifier) : ServerAuthenticationConverter {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun convert(swe: ServerWebExchange): Mono<Authentication> = Mono.justOrEmpty(swe)
            .flatMap { serverWebExchange -> extractBearerTokenFromAuthorizationHeader(serverWebExchange) }
            .doOnNext { log.debug("--- JWT token: {}", it) }
            .map { token -> Pair(token, jwtTokenVerifier.verify(token)) }
            .doOnNext { log.debug("--- JWT token verifyed: {}", it) }
            .map { pairTokenAndClaims -> jwtTokenVerifier.getAuthentication(pairTokenAndClaims.first) }
            .doOnNext { log.debug("--- Authentication created: $it") }
            .doOnError { error -> throw BadCredentialsException("Invalid JWT", error) }


    /**
     * Extract Bearer token form Authorization Header (Authorization: Bearer eyJhbGciOiJIU.eyJpc3MiOiJIZWxzc.GciOiJIUSGciO)
     *
     * @param serverWebExchange ServerWebExchange
     * @return Mono<String>
     */
    private fun extractBearerTokenFromAuthorizationHeader(serverWebExchange: ServerWebExchange): Mono<String> {
        fun  extractTokenFromBearer(bearerToken: String?) =
                if (bearerToken != null && bearerToken.startsWith(AUTHENTICATION_SCHEMA)) {
                    Mono.justOrEmpty(bearerToken.substring(AUTHENTICATION_SCHEMA.length, bearerToken.length).trim())
                } else Mono.empty()

        return extractTokenFromBearer(serverWebExchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION).toString()
                .also { log.trace("--- Found Authorization Header: $it") })
    }
}

