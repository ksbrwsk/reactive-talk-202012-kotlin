package people

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class PersonRouter {

    val baseUrl = "api/people"

    @Bean
    fun http(personHandler: PersonHandler) = coRouter {
        baseUrl.nest {
            GET("") { personHandler.handleFindAll(it) }
            GET("/{id}") { personHandler.handleFindById(it) }
            GET("/byName/{name}") { personHandler.handleFindFirstByName(it) }
            DELETE("/{id}") { personHandler.handleDeleteById(it) }
            POST("") { personHandler.handleSave(it) }
        }
    }
}