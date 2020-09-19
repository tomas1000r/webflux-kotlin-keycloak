package com.example.demoapp.user

import com.example.demoapp.adapter.db.repository.admin.AdminUserRepository
import com.example.demoapp.adapter.db.repository.standard.StandardUserRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/users")
class UserController(
        private val adminUserRepository: AdminUserRepository,
        private val standardUserRepository: StandardUserRepository
) {

    @PreAuthorize("hasRole('ROLE_USERADMIN')")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): Mono<Void> {
        return adminUserRepository.deleteById(id)
    }

    @PreAuthorize("hasRole('ROLE_USERADMIN')")
    @GetMapping
    fun findAll(): Flux<UserDto> {
        return adminUserRepository.findAll()
                .map { u -> UserDto(u.id, u.name) }
    }

    @GetMapping("/me")
    fun findOne(@AuthenticationPrincipal principal: OAuth2AuthenticatedPrincipal): Mono<UserDto> {
        // TODO Load user from db by principal

//        return standardUserRepository.findById(1)
//                .map { u -> UserDto(u.id, u.name) }

        return Mono.just(UserDto(0, principal.name))
    }
}
