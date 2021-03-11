package ch.keepcalm.security.client

import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.security.core.context.SecurityContextHolder

/**
 * RestTemplate JWT token propagation
 *
 * Can be added to a RestTemplate instance like this:
 *
 * ```
 *   restTemplate.getInterceptors().add(JwtTokenRelayHttpRequestInterceptor())
 * ```
 */
class JwtTokenRelayHttpRequestInterceptor : ClientHttpRequestInterceptor {

    override fun intercept(request: HttpRequest, body: ByteArray, execution: ClientHttpRequestExecution):
        ClientHttpResponse {

            if (!request.headers.containsKey(AUTHORIZATION)) {
                SecurityContextHolder.getContext().authentication?.apply {
                    request.headers.set(AUTHORIZATION, "Bearer $credentials")
                }
            }
            return execution.execute(request, body)
        }
}
