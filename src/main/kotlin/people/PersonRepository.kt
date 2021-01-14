package people

import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface PersonRepository : CoroutineCrudRepository<Person, Long> {

    suspend fun findFirstByName(name: String): Person
}