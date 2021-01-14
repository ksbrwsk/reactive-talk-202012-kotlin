package people

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@WebFluxTest
@ContextConfiguration(classes = [PersonHandler::class, PersonRouter::class])
class PersonHandlerTest(@Autowired var applicationContext: ApplicationContext) {

    private val baseUrl = "/api/people"

    private lateinit var webTestClient: WebTestClient

    @MockBean
    lateinit var personRepository: PersonRepository;

    @BeforeEach
    fun setUp() {
        webTestClient = WebTestClient.bindToApplicationContext(applicationContext).build()
    }

    @Test
    @DisplayName("should handle request find all")
    fun should_handle_find_all() {
        runBlocking {
            val list = flowOf(Person(1L, "Name"), Person(2L, "Sabo"))
            given(personRepository.findAll()).willReturn(list)

            webTestClient
                .get()
                .uri(baseUrl)
                .exchange()
                .expectStatus()
                .is2xxSuccessful
                .expectBody()
                .jsonPath("@.[0].name")
                .isEqualTo("Name")
                .jsonPath("@.[1].name")
                .isEqualTo("Sabo")
        }
    }

    @Test
    @DisplayName("should handle find by id x")
    fun should_handle_find_by_id() {
        runBlocking {
            val expected = Person(1L, "Name")
            given(personRepository.findById(1L)).willReturn(expected)

            val actual = webTestClient
                .get()
                .uri("$baseUrl/1")
                .exchange()
                .expectStatus()
                .is2xxSuccessful
                .expectBody<Person>()
                .returnResult()
                .responseBody
            assertEquals(expected, actual)
        }
    }

    @Test
    @DisplayName("should handle find first by name x")
    fun should_handle_find_first_by_name() {
        runBlocking {
            val expected = Person(1L, "Name")
            given(personRepository.findFirstByName("Name")).willReturn(expected)

            val actual = webTestClient
                .get()
                .uri("$baseUrl/byName/Name")
                .exchange()
                .expectStatus()
                .is2xxSuccessful
                .expectBody<Person>()
                .returnResult()
                .responseBody
            assertEquals(expected, actual)
        }
    }

    @Test
    @DisplayName("should delete person by id x")
    fun should_delete_person_by_id() {
        runBlocking {
            val person = Person(1L, "Name")
            given(personRepository.findById(1L)).willReturn(person)

            webTestClient
                .delete()
                .uri("$baseUrl/1")
                .exchange()
                .expectStatus()
                .isOk
                .expectBody<String>()
                .returnResult()
                .responseBody
                .equals("Deleted successfully!")
        }
    }

    @Test
    @DisplayName("should handle unknwon URL")
    fun should_handle_not_found() {
        this.webTestClient
            .get()
            .uri("/api/peple")
            .exchange()
            .expectStatus()
            .is4xxClientError
    }

    @Test
    @DisplayName("should handle request save person")
    fun should_handle_save_person() {
        runBlocking {
            val expected = Person(1L, "Name")
            given(personRepository.save(expected)).willReturn(expected)

            val actual = webTestClient
                .post()
                .uri(baseUrl)
                .bodyValue(expected)
                .exchange()
                .expectStatus()
                .is2xxSuccessful
                .expectBody<Person>()
                .returnResult()
                .responseBody
            assertNotNull(actual)
            assertEquals(expected, actual)
        }
    }
}
