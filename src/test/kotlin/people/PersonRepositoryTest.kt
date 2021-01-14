package people

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

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
    fun should_load_all_people() {
        runBlocking {
            val all = personRepository.findAll().toList()
            assertFalse(all.isEmpty() )
        }
    }

    @Test
    @DisplayName("should load person by id x")
    fun should_load_person_by_id() {
        runBlocking {
            val person = personRepository.findById(1L)
            assertNotNull(person)
        }
    }

    @Test
    @DisplayName("should load person by name x")
    fun should_load_person_by_name() {
        runBlocking {
            val person = personRepository.findFirstByName("Person@2")
            assertNotNull(person)
            assertEquals("Person@2", person.name)
        }
    }

    @Test
    @DisplayName("should be empty result")
    fun should_be_empty_result() {
        runBlocking {
            val empty = personRepository.findById(11111111L)
            assertNull(empty)
        }
    }

    @Test
    @DisplayName("should persist person")
     fun should_persist_person() {
        runBlocking {
            val person = personRepository.save(Person(null, "Sabo"))
            assertTrue(person.id != null)
            assertEquals("Sabo", person.name)
        }
    }
}