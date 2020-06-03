package com.example.demoapp.app.user

import com.example.demoapp.adapter.cache.entity.RegistrationLink
import com.example.demoapp.adapter.cache.repository.RegistrationLinkRepository
import com.example.demoapp.adapter.db.entity.User
import com.example.demoapp.adapter.db.repository.standard.StandardUserRepository
import com.example.demoapp.app.config.ApplicationProperties
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*

internal class UserRegistrationWorkflowTest {

    companion object {
        lateinit var uut: UserRegistrationWorkflow
    }

    @BeforeEach
    internal fun setUp() {
        uut = UserRegistrationWorkflow(
                mockApplicationProperties(),
                mockRegistrationLinkRepository(),
                mockStandardUserRepository()
        )
    }

    @Test
    internal fun shouldBeginRegistration() {
        val linkMono = uut.beginRegistration(UserRegistrationDto("first", "last", "test@email.com"))

        StepVerifier.create(linkMono)
                .assertNext { link -> assertEquals("http://localhost/api/users/complete-registration/123-456-789", link) }
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

    private fun mockApplicationProperties(): ApplicationProperties {
        val properties = mockk<ApplicationProperties>()

        every { properties.completeRegistrationUrl } returns "http://localhost/api/users/complete-registration"

        return properties
    }

    private fun mockRegistrationLinkRepository(): RegistrationLinkRepository {
        val repository = mockk<RegistrationLinkRepository>()

        every { repository.create(ofType(RegistrationLink::class)) } returns Mono.just(RegistrationLink("123-456-789", "test@email.com"))
        every { repository.findByUuid(ofType(UUID::class)) } returns Mono.just(RegistrationLink("123-456-789", "test@email.com"))

        return repository
    }

    private fun mockStandardUserRepository(): StandardUserRepository {
        val repository = mockk<StandardUserRepository>()

        every { repository.save(ofType(User::class)) } returns Mono.just(User(0, "test"))

        return repository
    }
}
