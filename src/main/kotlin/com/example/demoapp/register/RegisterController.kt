package com.example.demoapp.register

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
        val details = RegistrationDetails(dto.firstName, dto.lastName, dto.email, dto.password);
        return registerService.beginRegistration(details)
    }

    @GetMapping("/{uuid}")
    fun completeRegistration(@PathVariable uuid: UUID): Mono<Void> {
        return registerService.confirmRegistration(uuid)
    }
}
