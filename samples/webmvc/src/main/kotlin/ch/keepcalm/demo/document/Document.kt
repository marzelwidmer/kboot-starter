package ch.keepcalm.demo.document

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
data class Document(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long, val content: String, val owner: String)

@Repository
interface DocumentRepository : CrudRepository<Document?, Long?> {
    fun findById(id: Long?): Document?
}

@Service
class DocumentService(private val documentRepository: DocumentRepository) {
    fun getDocumentById(id: Long): Document? = documentRepository.findById(id = id)
}

@RestController
@RequestMapping("/api/document")
class DocumentController(private val documentService: DocumentService) {
    @GetMapping("/{id}")
    fun getDocumentById(@PathVariable id: Long): Document? = documentService.getDocumentById(id)
}
