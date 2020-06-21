package com.example.demoapp.user

import com.example.demoapp.adapter.db.repository.admin.AdminUserRepository
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/users")
class UserController(
        private val adminUserRepository: AdminUserRepository
) {

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): Mono<Void> {
        return adminUserRepository.deleteById(id)
    }

    @GetMapping
    fun findAll(): Flux<UserDto> {
        return adminUserRepository.findAll()
                .map { u -> UserDto(u.id, u.name) }
    }
}
