package ch.keepcalm.security.mock.base

import ch.keepcalm.security.mock.TestController
import io.restassured.config.EncoderConfig
import io.restassured.module.mockmvc.RestAssuredMockMvc
import io.restassured.module.mockmvc.config.MockMvcConfig.mockMvcConfig
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig
import org.junit.jupiter.api.BeforeEach

open class OpaBase {

    @BeforeEach
    fun setUp() {
        val encoderConfig: EncoderConfig = EncoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)
        RestAssuredMockMvc.config = RestAssuredMockMvcConfig().mockMvcConfig(
            mockMvcConfig().dontAutomaticallyApplySpringSecurityMockMvcConfigurer()
        ).encoderConfig(encoderConfig)
        RestAssuredMockMvc.standaloneSetup(TestController())
    }
}
