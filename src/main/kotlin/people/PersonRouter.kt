package people

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class PersonRouter {

    val baseUrl = "api/people"

    @Bean
    fun http(personHandler: PersonHandler) = router {
        GET(baseUrl) {
            personHandler.handleFindAll(it)
        }
        GET("$baseUrl/{id}") {
            personHandler.handleFindById(it)
        }
        DELETE("$baseUrl/{id}") {
            personHandler.handleDeleteById(it)
        }
        GET("$baseUrl/byName/{name}") {
            personHandler.handleFindFirstByName(it)
        }
        POST(baseUrl) {
            personHandler.handleSave(it)
        }
    }
}