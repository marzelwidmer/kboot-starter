package ch.keepcalm.demo.salary

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Salary(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long, val username: String, val amount: String)

@Repository
interface SalaryRepository : CrudRepository<Salary?, Long?> {
    fun findByUsername(username: String?): Salary?
}

@Service
class SalaryService(private val salaryRepository: SalaryRepository) {
    fun getSalaryByUsername(username: String): Salary? = salaryRepository.findByUsername(username = username)
}

@RestController
@RequestMapping("/api/salary")
class SalaryController(private val salaryService: SalaryService) {
    @GetMapping("/{username}")
    fun getSalaryByUsername(@PathVariable username: String) = salaryService.getSalaryByUsername(username)
}
