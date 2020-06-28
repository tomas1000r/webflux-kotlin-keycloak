package com.example.demoapp.app.register

import com.example.demoapp.adapter.db.entity.User
import com.example.demoapp.register.RegisterController
import com.example.demoapp.register.RegisterDto
import com.example.demoapp.register.RegisterService
import com.example.demoapp.register.RegistrationDetails
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
        val monoVoid = uut.completeRegistration(UUID.randomUUID())

        StepVerifier.create(monoVoid)
                .verifyComplete()
    }

    private fun mockUserRegistrationWorkflow(): RegisterService {
        val workflow = mockk<RegisterService>()

        every { workflow.beginRegistration(ofType(RegistrationDetails::class)) } returns Mono.just("123-456-789")
        every { workflow.confirmRegistration(ofType(UUID::class)) } returns Mono.empty()

        return workflow
    }
}
