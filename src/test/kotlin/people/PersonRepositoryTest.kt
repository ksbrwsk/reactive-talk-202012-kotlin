package people

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import reactor.test.StepVerifier

@DataR2dbcTest
@Testcontainers
internal class PersonRepositoryTest {

    @Autowired
    lateinit var personRepository: PersonRepository

    companion object {

        @Container
        val container = KPostgreSQLContainer("postgres:12")

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.r2dbc.url", container::getR2dbcUrl);
            registry.add("spring.r2dbc.username", container::getUsername);
            registry.add("spring.r2dbc.password", container::getPassword);
            registry.add("spring.flyway.url", container::getJdbcUrl);
            registry.add("spring.flyway.user", container::getUsername);
            registry.add("spring.flyway.password", container::getPassword);
        }
    }

    @Test
    @DisplayName("should load all people")
    internal fun should_load_all_people() {
        val allPeople = this.personRepository.findAll()
        StepVerifier
            .create(allPeople)
            .expectNextCount(108) // V2_0__data.sql contains 108 rows
            .verifyComplete()
    }

    @Test
    @DisplayName("should load person by id x")
    internal fun should_load_person_by_id() {
        val person = this.personRepository.findById(1L)
        StepVerifier
            .create(person)
            .expectNextMatches { it.id == 1L && it.name == "Person@1" }
            .verifyComplete()
    }

    @Test
    @DisplayName("should load person by name x")
    internal fun should_load_person_by_name() {
        val person = this.personRepository.findFirstByName("Person@2")
        StepVerifier
            .create(person)
            .expectNextMatches { it.name == "Person@2" }
            .verifyComplete()
    }

    @Test
    @DisplayName("should be empty result")
    internal fun should_be_empty_result() {
        val empty = this.personRepository.findById(11111111L)
        StepVerifier
            .create(empty)
            .verifyComplete()
    }

    @Test
    @DisplayName("should persist person")
    internal fun should_persist_person() {
        val person = this.personRepository
            .save(Person(null, "Sabo"))
            .flatMap { this.personRepository.findById(it.id!!) }
        StepVerifier
            .create(person)
            .expectNextMatches { it.name == "Sabo" }
            .verifyComplete()
    }
}