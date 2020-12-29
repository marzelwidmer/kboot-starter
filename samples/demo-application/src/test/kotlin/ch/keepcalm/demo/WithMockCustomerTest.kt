package ch.keepcalm.demo

import ch.keepcalm.security.*
import ch.keepcalm.security.test.WithMockCustomUser
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class WithMockCustomerTest {

	@WithMockCustomUser(username = "jane@doe.ch", authorities = ["keepcalm.admin", "keepcalm.user"], firstname = "jane", lastname = "doe")
	@Test
	fun `test SecurityContext of Jane Doe`() {
		assertThat(getPrincipalFirstName()).isEqualTo("jane")
		assertThat(getPrincipalLastName()).isEqualTo("doe")
		assertThat(getCurrentUsername()).isEqualTo("jane@doe.ch")
	}
}
