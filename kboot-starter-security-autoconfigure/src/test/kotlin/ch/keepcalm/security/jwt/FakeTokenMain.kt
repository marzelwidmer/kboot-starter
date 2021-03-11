@file:JvmName("FakeTokenMain")
package ch.keepcalm.security.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

fun main() {
    println("Welcome to JWT token generator....")
    println("subject : [john.doe@keepcalm.ch]")
    val subject = readLine()?.ifBlank { Token().subject }
    println("firstname : [John]")
    val firstname = readLine()?.ifBlank { Token().firstname }
    println("lastname : [Doe]")
    val lastname = readLine()?.ifBlank { Token().lastname }
    println("roles : [keepcalm.user, keepcalm.admin]")
    val roles = readLine()?.ifBlank { Token().roles }
    println("issuer : [Keepcalm Auth]")
    val issuer = readLine()?.ifBlank { Token().issuer }
    println("audience : [Keepcalm]")
    val audience = readLine()?.ifBlank { Token().audience }
    println("secret : [thisIsAVerySecureAndLongJWTSecretThatCanBeUsedForTesting]")
    val secret = readLine()?.ifBlank { Token().secret }.toString()
    println("userEmail : [joh.doe@keepcalm.ch]")
    val userEmail = readLine()?.ifBlank { Token().userEmail }
    println("language : [de]")
    val language = readLine()?.ifBlank { Token().language }
    println("expiration : [99999999]")
    val expiration = readLine()?.toIntOrNull() ?: Token().expiration

    val token = Token(
        subject = subject,
        firstname = firstname,
        language = language,
        lastname = lastname,
        roles = roles,
        issuer = issuer,
        audience = audience,
        secret = secret,
        userEmail = userEmail,
        expiration = expiration
    )

    val generatedToken = generateToken(token)
    println("###############################")
    println(" \n \"${generatedToken}\" \n")
    println("###############################")

    println("-----------------")
    println("export token=\"${generatedToken}\" \n")
    println(" http :9009/api/customers \"Authorization:Bearer  \$token\" -v \n")
    println("-----------------")

    println("###############################")
    println("export token=\"${generatedToken}\" \n")
    println(
        "\ncurl http://localhost:9009/api/customers " +
            "-H \"Authorization:Bearer  \$token\" -v  | python -m json.tool \n"
    )
    println("###############################")
}

fun generateToken(token: Token): String =
    Jwts.builder()
        .setId(UUID.randomUUID().toString())
        .setSubject(token.subject)
        .setIssuedAt(Date())
        .setExpiration(
            Date.from(
                token.expiration.toLong().let {
                    LocalDateTime.now().plusSeconds(it).atZone(ZoneId.systemDefault()).toInstant()
                }
            )
        )
        .setIssuer(token.issuer)
        .setAudience(token.audience)
        .addClaims(
            mapOf(
                Pair("language", token.language),
                Pair("lastname", token.lastname),
                Pair("firstname", token.firstname),
                Pair("email", token.userEmail),
                Pair("roles", token.roles)
            )
        ).signWith(
            Keys.hmacShaKeyFor(token.secret.toByteArray(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256
        )
        .compact()

data class Token(
    var language: String? = "de",
    var firstname: String? = "John",
    var lastname: String? = "Doe",
    var subject: String? = "john.doe@keepcalm.ch",
    var roles: String? = "keepcalm.user",
    var issuer: String? = "Keepcalm Auth",
    var audience: String? = "Keepcalm",
    var secret: String = "thisIsAVerySecureAndLongJWTSecretThatCanBeUsedForTesting",
    var userEmail: String? = "joh.doe@keepcalm.ch",
    var expiration: Int = 999999999
)
