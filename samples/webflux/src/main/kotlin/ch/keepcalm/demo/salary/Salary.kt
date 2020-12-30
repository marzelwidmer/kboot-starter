package ch.keepcalm.demo.salary

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/salary")
class SalaryController() {
    @GetMapping("/{username}")
    fun getSalaryByUsername(@PathVariable username: String) = "Salary username $username"
}
