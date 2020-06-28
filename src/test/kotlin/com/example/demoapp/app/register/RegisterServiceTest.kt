package com.example.demoapp.app.register

import com.example.demoapp.adapter.cache.RegistrationMetadata
import com.example.demoapp.adapter.cache.RegistrationMetadataRepository
import com.example.demoapp.adapter.db.entity.User
import com.example.demoapp.adapter.db.repository.standard.StandardUserRepository
import com.example.demoapp.adapter.keycloak.KeycloakRepository
import com.example.demoapp.config.AppProperties
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

internal class RegisterServiceTest {

    companion object {
        lateinit var uut: RegisterService
    }

    @BeforeEach
    internal fun setUp() {
        uut = RegisterService(
                mockApplicationProperties(),
                mockRegistrationLinkRepository(),
                mockStandardUserRepository(),
                mockKeycloakRepository()
        )
    }

    @Test
    internal fun shouldBeginRegistration() {
        val linkMono = uut.beginRegistration(RegistrationDetails("first", "last", "test@email.com", "1234"))

        StepVerifier.create(linkMono)
                .assertNext { link -> assertEquals("http://localhost/api/register/123-456-789", link) }
                .verifyComplete()
    }

    @Test
    internal fun shouldCompleteRegistration() {
        val voidMono = uut.confirmRegistration(UUID.randomUUID())

        StepVerifier.create(voidMono)
                .verifyComplete()
    }

    private fun mockApplicationProperties(): AppProperties {
        val properties = mockk<AppProperties>()

        every { properties.confirmRegistrationUrl } returns "http://localhost/api/register"

        return properties
    }

    private fun mockRegistrationLinkRepository(): RegistrationMetadataRepository {
        val repository = mockk<RegistrationMetadataRepository>()

        every { repository.create(ofType(RegistrationMetadata::class)) } returns Mono.just(RegistrationMetadata("123-456-789", "test@email.com"))
        every { repository.findByUuid(ofType(UUID::class)) } returns Mono.just(RegistrationMetadata("123-456-789", "test@email.com"))

        return repository
    }

    private fun mockStandardUserRepository(): StandardUserRepository {
        val repository = mockk<StandardUserRepository>()

        every { repository.save(ofType(User::class)) } returns Mono.just(User(0, "test"))

        return repository
    }

    private fun mockKeycloakRepository(): KeycloakRepository {
        val repository = mockk<KeycloakRepository>()

        // TODO finish mock

        return repository
    }
}
