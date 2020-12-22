package people

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.web.reactive.function.BodyInserters
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@WebFluxTest
@ContextConfiguration(classes = [PersonHandler::class, PersonRouter::class])
internal class PersonHandlerTest(@Autowired var applicationContext: ApplicationContext) {

    private val BASE_URL = "/api/people"

    @MockkBean
    lateinit var personRepository: PersonRepository;

    private lateinit var webTestClient: WebTestClient

    @BeforeEach
    fun setUp() {
        webTestClient = WebTestClient.bindToApplicationContext(applicationContext).build()
    }

    @Test
    @DisplayName("should handle request find all")
    fun should_handle_find_all() {

        every { personRepository.findAll() } returns Flux.just(
            Person(1L, "Name"),
            Person(2L, "Sabo")
        )

        this.webTestClient
            .get()
            .uri(BASE_URL)
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .expectBody()
            .jsonPath("@.[0].name")
            .isEqualTo("Name")
            .jsonPath("@.[1].name")
            .isEqualTo("Sabo")
    }

    @Test
    @DisplayName("should handle find by id x")
    fun should_handle_find_by_id() {

        every { personRepository.findById(1L) } returns Mono.just(Person(1L, "Name"))

        this.webTestClient
            .get()
            .uri(BASE_URL + "/1")
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .expectBody<Person>()
            .isEqualTo(Person(1L, "Name"))
    }

    @Test
    @DisplayName("should handle find first by name x")
    fun should_handle_find_first_by_name() {

        every { personRepository.findFirstByName("Name") } returns Mono.just(Person(1L, "Name"))

        this.webTestClient
            .get()
            .uri(BASE_URL + "/byName/Name")
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .expectBody<Person>()
            .isEqualTo(Person(1L, "Name"))
    }

    @Test
    @DisplayName("should delete person by id x")
    internal fun should_delete_person_by_id() {

        val person = Person(1L, "Name")
        every { personRepository.findById(1L) } returns Mono.just(person)
        every { personRepository.delete(person) } returns Mono.empty();

        this.webTestClient
            .delete()
            .uri(BASE_URL + "/1")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody<String>()
            .returnResult()
            .responseBody
            .equals("Deleted successfully!")
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
        val person = Person(1L, "Name")
        val personMono = Mono.just(person)
        every { personRepository.save(person) } returns personMono
        val result = webTestClient
            .post()
            .uri(BASE_URL)
            .body(BodyInserters.fromValue<Any>(person))
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .expectBody<Person>()
            .returnResult()
            .responseBody
        assertNotNull(result)
        assertEquals(person, result)
    }
}
