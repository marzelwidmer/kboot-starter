package ch.keepcalm.security

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MockApplicationTest
fun main(args: Array<String>) {
    runApplication<MockApplicationTest>(*args)
}
