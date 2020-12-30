package ch.keepcalm.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@SpringBootApplication
class WebmvcApplication

fun main(args: Array<String>) {
    runApplication<WebmvcApplication>(*args)
}
