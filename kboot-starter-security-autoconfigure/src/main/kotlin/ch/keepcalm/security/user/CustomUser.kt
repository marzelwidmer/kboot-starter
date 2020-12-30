package ch.keepcalm.security.user

import io.jsonwebtoken.Claims
import org.springframework.security.core.authority.AuthorityUtils

class CustomUser {

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

    internal fun createPrincipal(claims: Claims) =
        CustomUserDetails(
            subject = claims.subject,
            password = "",
            firstname = extractFirstNameFromClaims(claims),
            lastname = extractLastNameFromClaims(claims),
            authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(
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
