package ch.keepcalm.demo.document // ktlint-disable filename

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/document")
class DocumentController {
    @GetMapping("/{id}")
    fun getDocumentById(@PathVariable id: Long) = "Document id  $id"
}
