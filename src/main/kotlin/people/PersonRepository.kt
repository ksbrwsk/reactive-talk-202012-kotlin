package people

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface PersonRepository : ReactiveCrudRepository<Person, Long> {

    fun findFirstByName(name: String): Mono<Person>
}