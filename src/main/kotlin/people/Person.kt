package people

import org.springframework.data.annotation.Id

data class Person(@Id val id: Long?, val name: String)