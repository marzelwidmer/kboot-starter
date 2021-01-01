package ch.keepcalm.security.test

import org.springframework.security.test.context.support.WithSecurityContext
import java.lang.annotation.Inherited

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@MustBeDocumented
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory::class)
annotation class WithMockCustomUser(
    val username: String = "joh.doe@keepcalm.ch",
    val password: String = "JWT-TOKEN",
    val authorities: Array<String> = ["keepcalm.user"],
    val firstname: String = "John",
    val lastname: String = "Doe",
    val language: String = "de"
)
