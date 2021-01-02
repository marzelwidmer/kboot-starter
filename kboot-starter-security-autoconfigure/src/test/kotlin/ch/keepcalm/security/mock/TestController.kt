package ch.keepcalm.security.mock

import org.springframework.web.bind.annotation.*

@RestController
@ResponseBody()
class TestController {

    @GetMapping("/unsecure")
    fun unsecure(): String {
        return "Welcome to the Unsecure Endpoint."
    }

    @GetMapping("/api/user/foo")
    fun secureUser(): String {
        return "Welcome to the Secure User Endpoint."
    }

    @GetMapping("/api/admin")
    fun secureAdmin(): String {
        return "Welcome to the Secure Admin Endpoint."
    }

    @PostMapping("/v1/data/http/authz/allow")
    fun opaPolicyAllow(): OpaResponse {
        return OpaResponse()
    }
}

data class OpaResponse(val result: String = "true")
