package ch.keepcalm.security.test

import ch.keepcalm.security.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@SpringBootConfiguration
class WithMockCustomerTest {

	@WithMockCustomUser(username = "jane@doe.ch", authorities = ["keepcalm.admin", "keepcalm.user"], firstname = "jane", lastname = "doe")
	@Test
	fun `test SecurityContext of Jane Doe`() {
		assertThat(getPrincipalFirstName()).isEqualTo("jane")
		assertThat(getPrincipalLastName()).isEqualTo("doe")
		assertThat(getCurrentUsername()).isEqualTo("jane@doe.ch")
	}
}
