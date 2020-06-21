package com.example.demoapp.app.register

import com.example.demoapp.adapter.db.entity.User
import com.example.demoapp.register.RegisterController
import com.example.demoapp.register.RegisterService
import com.example.demoapp.register.RegisterDto
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*

internal class RegisterControllerTest {

    companion object {
        lateinit var uut: RegisterController
    }

    @BeforeEach
    internal fun setUp() {
        uut = RegisterController(mockUserRegistrationWorkflow())
    }

    @Test
    internal fun shouldBeginRegistration() {
        val uuidMono = uut.initRegistration(
                RegisterDto("firstname", "lastname", "test@email.com"))

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

    private fun mockUserRegistrationWorkflow(): RegisterService {
        val workflow = mockk<RegisterService>()

        every { workflow.beginRegistration(ofType(RegisterDto::class)) } returns Mono.just("123-456-789")
        every { workflow.completeRegistration(ofType(UUID::class)) } returns Mono.just(User(0, "test"))

        return workflow
    }
}
