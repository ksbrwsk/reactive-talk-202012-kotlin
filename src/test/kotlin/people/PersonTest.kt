package people

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class PersonTest {

    @Test
    @DisplayName("should create a person")
    private fun shoud_create_person() {
        val person = Person(1L, "Name")
        assertThat(person.id?.equals(1L))
        assertThat(person.name == "Name")
    }
}