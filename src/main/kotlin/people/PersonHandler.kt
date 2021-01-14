package people

import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.notFound
import org.springframework.web.reactive.function.server.ServerResponse.ok

@Component
class PersonHandler(val personRepository: PersonRepository) {

    val log = LoggerFactory.getLogger(PersonHandler::class.java)

    suspend fun handleFindAll(serverRequest: ServerRequest): ServerResponse {
        log.info("Handle request ${serverRequest.methodName()} ${serverRequest.path()}")
        return ok()
            .bodyAndAwait(personRepository.findAll())
    }

    suspend fun handleFindById(serverRequest: ServerRequest): ServerResponse {
        log.info("Handle request ${serverRequest.methodName()} ${serverRequest.path()}")
        val id = serverRequest.pathVariable("id").toLong()
        val person = personRepository.findById(id)
        return when {
            person != null -> ok().bodyValueAndAwait(person)
            else -> notFound().buildAndAwait()
        }
    }

    suspend fun handleFindFirstByName(serverRequest: ServerRequest): ServerResponse {
        log.info("Handle request ${serverRequest.methodName()} ${serverRequest.path()}")
        val name = serverRequest.pathVariable("name")
        return ok()
            .bodyValueAndAwait(personRepository.findFirstByName(name))
    }

    suspend fun handleDeleteById(serverRequest: ServerRequest): ServerResponse {
        log.info("Handle request ${serverRequest.methodName()}  ${serverRequest.path()}")
        val id = serverRequest.pathVariable("id").toLong()
        val person = personRepository.findById(id)
        person?.let {
            personRepository.delete(it)
            return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValueAndAwait("Successfully deleted!")
        }
        return notFound().buildAndAwait()
    }

    suspend fun handleSave(serverRequest: ServerRequest): ServerResponse {
        log.info("Handle request ${serverRequest.methodName()} ${serverRequest.path()}")
        val person = serverRequest.awaitBody<Person>()
        return ok()
            .bodyValueAndAwait(personRepository.save(person))
    }
}