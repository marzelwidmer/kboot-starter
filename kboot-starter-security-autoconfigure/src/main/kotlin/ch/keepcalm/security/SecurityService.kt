package ch.keepcalm.security

import org.springframework.stereotype.Service

@Service
class SecurityService {
    fun getPrincipal() = ch.keepcalm.security.securityPrincipal()
    fun isPrincipalUser() = ch.keepcalm.security.securityIsPrincipalUser()
    fun getBearerFromSecurityContext() = ch.keepcalm.security.securityCurrentUserBearerToken()
    fun getAuthorities() = ch.keepcalm.security.securityGetAuthorities()
    fun getRoles() = ch.keepcalm.security.securityGetRoles()
    fun getPrincipalFirstName() = ch.keepcalm.security.securityUserFirstName()
    fun getPrincipalLastName() = ch.keepcalm.security.securityUserName()
    fun getAuthenticationName() = ch.keepcalm.security.securityGetAuthenticationName()
    fun securityGetPortalAccountId() = ch.keepcalm.security.securityGetPortalAccountId()
}
