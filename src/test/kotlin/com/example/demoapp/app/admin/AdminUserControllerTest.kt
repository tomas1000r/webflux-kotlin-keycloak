package com.example.demoapp.app.admin

import com.example.demoapp.adapter.db.entity.User
import com.example.demoapp.adapter.db.repository.admin.AdminUserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

internal class AdminUserControllerTest {

    companion object {
        lateinit var uut: AdminUserController
    }

    @BeforeEach
    internal fun setUp() {
        uut = AdminUserController(mockAdminUserRepository())
    }

    @Test
    internal fun shouldDelete() {
        val deleteMono = uut.delete(0)

        StepVerifier.create(deleteMono)
                .verifyComplete()
    }

    @Test
    internal fun shouldFindAll() {
        val usersMono = uut.findAll()

        StepVerifier.create(usersMono)
                .assertNext { user ->
                    assertEquals(0, user.id)
                    assertEquals("test", user.name)
                }
                .verifyComplete()
    }

    private fun mockAdminUserRepository(): AdminUserRepository {
        val repository = mockk<AdminUserRepository>()

        every { repository.deleteById(ofType(Long::class)) } returns Mono.just("").then()
        every { repository.findAll() } returns Flux.just(User(0, "test"))

        return repository
    }
}
