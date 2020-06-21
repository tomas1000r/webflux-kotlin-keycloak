package com.example.demoapp.register

import com.example.demoapp.user.UserDto
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/register")
class RegisterController(
        private val registerService: RegisterService
) {

    @PostMapping
    fun initRegistration(@RequestBody @Valid dto: RegisterDto): Mono<String> {
        return registerService.beginRegistration(dto)
    }

    @PostMapping("/{uuid}")
    fun completeRegistration(@PathVariable uuid: UUID): Mono<UserDto> {
        return registerService.completeRegistration(uuid)
                .map { user -> UserDto(user.id, user.name) }
    }
}
