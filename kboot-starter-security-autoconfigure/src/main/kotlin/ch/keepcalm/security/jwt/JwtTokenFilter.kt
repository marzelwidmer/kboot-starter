package ch.keepcalm.security.jwt

import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils.hasText
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

class JwtTokenFilter(private val tokenVerifier: JwtTokenVerifier) : GenericFilterBean() {
    companion object {
        private val LOG = LoggerFactory.getLogger(JwtTokenFilter::class.java)
        private const val AUTHENTICATION_SCHEMA = "Bearer "
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {

        authenticateIfAvailable(resolveToken(servletRequest as HttpServletRequest))
        filterChain.doFilter(servletRequest, servletResponse)
    }

    private fun authenticateIfAvailable(jwt: String?) =
            jwt.takeIf { hasText(jwt) }?.apply {
                if (tokenVerifier.validateToken(this)) {
                    SecurityContextHolder.getContext().authentication = tokenVerifier.getAuthentication(this)
                }
            }

    private fun resolveToken(request: HttpServletRequest) =
            toJwtToken(request.getHeader(AUTHORIZATION)
                    .also { LOG.trace("Found Authorization Header: $it") })

    private fun toJwtToken(bearerToken: String?) =
            if (bearerToken != null && bearerToken.startsWith(AUTHENTICATION_SCHEMA)) {
                bearerToken.substring(AUTHENTICATION_SCHEMA.length, bearerToken.length).trim()
                        .also { LOG.debug("Found JWT token: $it") }
            } else null
}
