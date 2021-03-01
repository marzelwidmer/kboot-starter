package ch.keepcalm.security.test

import ch.keepcalm.security.*
import ch.keepcalm.security.securityUserName
import org.junit.jupiter.api.Test
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.amshove.kluent.`should be equal to`

@SpringBootTest
@SpringBootConfiguration
class WithMockCustomerTest {

    @WithMockCustomUser(username = "jane@doe.ch", authorities = ["keepcalm.admin", "keepcalm.user"], firstname = "jane", lastname = "doe")
    @Test
    fun `test SecurityContext of Jane Doe`() {
        securityUserFirstName() `should be equal to` "jane"
        securityUserFirstLastName() `should be equal to` "doe"
        securityCurrentUserName() `should be equal to` "jane@doe.ch"
    }
}
