package ch.keepcalm.security.authz

import ch.keepcalm.security.securityGetAuthenticationName
import ch.keepcalm.security.securityGetAuthorities
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.*
import org.springframework.security.access.AccessDecisionVoter
import org.springframework.security.access.ConfigAttribute
import org.springframework.security.core.Authentication
import org.springframework.security.web.FilterInvocation
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.util.*

@ConditionalOnProperty(prefix = "keepcalm.security", name = ["access-decision-voter.voters"], havingValue = "OPA")
@Component
class OPAVoter(private val uri: String = "http://localhost:8181/v1/data/authz/allow") : AccessDecisionVoter<FilterInvocation> {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    private val objectMapper = ObjectMapper()
    private val restTemplate = RestTemplate()

    override fun supports(configAttribute: ConfigAttribute?): Boolean {
        return true
    }

    override fun supports(clazz: Class<*>?): Boolean {
        return FilterInvocation::class.java.isAssignableFrom(clazz)
    }

    override fun vote(authentication: Authentication?, filterInvocation: FilterInvocation?, mutableCollection: MutableCollection<ConfigAttribute>?): Int {
        val name = securityGetAuthenticationName()
        val authorities = securityGetAuthorities()
        val method = filterInvocation?.request?.method

        val path: List<String>? = pathAsList(filterInvocation)
        val input: Map<String, Any?> = createInput(name, authorities, method, path)
        val requestNode: ObjectNode? = createRequestNode(input)
        val responseNode: JsonNode? = createResponseNode(requestNode)

        return isAccessGranted(responseNode)
    }

    private fun createResponseNode(requestNode: ObjectNode?): JsonNode? {
        val request = HttpEntity<String>(requestNode.toString(), HttpHeaders())
        val responseEntityStr: ResponseEntity<String> = restTemplate.postForEntity(uri, request, String::class.java)
        val responseNode: JsonNode? = Objects.requireNonNull(objectMapper.readTree(responseEntityStr.body))
        logAuthz("response", responseNode)
        return responseNode
    }

    private fun createRequestNode(input: Map<String, Any?>): ObjectNode? {
        val requestNode = objectMapper.createObjectNode()
        requestNode.set<JsonNode>("input", objectMapper.valueToTree(input))
        logAuthz("request", requestNode)
        return requestNode
    }

    private fun pathAsList(filterInvocation: FilterInvocation?): List<String>? = filterInvocation?.request?.requestURI?.replace("^/|/$", "")?.split("/")
    private fun createInput(name: String, authorities: Any?, method: String?, path: List<String>?) = mapOf(
        "name" to name,
        "authorities" to authorities,
        "method" to method,
        "path" to path
    )

    private fun logAuthz(reqOrResp: String, node: JsonNode?) {
        log.info(
            """Authorization $reqOrResp:
                    ${node?.toPrettyString()}
            """.trimIndent()
        )
    }

    private fun isAccessGranted(responseNode: JsonNode?) = if (responseNode?.has("result") == true && responseNode.get("result")?.asBoolean() == true) {
        AccessDecisionVoter.ACCESS_GRANTED
    } else {
        AccessDecisionVoter.ACCESS_DENIED
    }
}
