package com.example.demoapp.app.user

import com.example.demoapp.adapter.db.entity.User
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*

internal class StandardUserControllerTest {

    companion object {
        lateinit var uut: StandardUserController
    }

    @BeforeEach
    internal fun setUp() {
        uut = StandardUserController(mockUserRegistrationWorkflow())
    }

    @Test
    internal fun shouldBeginRegistration() {
        val uuidMono = uut.beginRegistration(
                UserRegistrationDto("firstname", "lastname", "test@email.com"))

        StepVerifier.create(uuidMono)
                .assertNext { uuid -> assertEquals("123-456-789", uuid) }
                .verifyComplete()
    }

    @Test
    internal fun shouldCompleteRegistration() {
        val userMono = uut.completeRegistration(UUID.randomUUID())

        StepVerifier.create(userMono)
                .assertNext { user ->
                    assertEquals(0, user.id)
                    assertEquals("test", user.name)
                }
                .verifyComplete()
    }

    private fun mockUserRegistrationWorkflow(): UserRegistrationWorkflow {
        val workflow = mockk<UserRegistrationWorkflow>()

        every { workflow.beginRegistration(ofType(UserRegistrationDto::class)) } returns Mono.just("123-456-789")
        every { workflow.completeRegistration(ofType(UUID::class)) } returns Mono.just(User(0, "test"))

        return workflow
    }
}
