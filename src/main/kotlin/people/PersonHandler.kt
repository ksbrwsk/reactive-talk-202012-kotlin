package people

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters.fromPublisher
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono

@Component
class PersonHandler(val personRepository: PersonRepository) {

    val log = LoggerFactory.getLogger(PersonHandler::class.java)

    fun handleFindAll(serverRequest: ServerRequest): Mono<ServerResponse> {
        log.info("Handle request ${serverRequest.methodName()} ${serverRequest.path()}")
        return ServerResponse
            .ok()
            .body(this.personRepository.findAll())
    }

    fun handleFindById(serverRequest: ServerRequest): Mono<ServerResponse> {
        log.info("Handle request ${serverRequest.methodName()} ${serverRequest.path()}")
        val id = serverRequest.pathVariable("id").toLong()
        return ServerResponse
            .ok()
            .body(this.personRepository.findById(id))
    }

    fun handleFindFirstByName(serverRequest: ServerRequest): Mono<ServerResponse> {
        log.info("Handle request ${serverRequest.methodName()} ${serverRequest.path()}")
        val name = serverRequest.pathVariable("name")
        return ServerResponse
            .ok()
            .body(this.personRepository.findFirstByName(name))
    }

    fun handleDeleteById(serverRequest: ServerRequest): Mono<ServerResponse> {
        log.info("Handle request ${serverRequest.methodName()} ${serverRequest.path()}")
        val id = serverRequest.pathVariable("id").toLong()
        val mono = this.personRepository.findById(id)
            .flatMap { this.personRepository.delete(it) }
            .thenReturn(Mono.just("Successfully deleted!"))
        return mono.flatMap { resp ->
            ServerResponse
                .ok()
                .body(resp, String::class.java)
        }
    }

    fun handleSave(serverRequest: ServerRequest): Mono<ServerResponse> {
        log.info("Handle request ${serverRequest.methodName()} ${serverRequest.path()}")
        val partnerMono = serverRequest.bodyToMono(Person::class.java)
        return ServerResponse
            .ok()
            .body(
                fromPublisher(
                    partnerMono
                        .flatMap(personRepository::save), Person::class.java
                )
            )
    }
}