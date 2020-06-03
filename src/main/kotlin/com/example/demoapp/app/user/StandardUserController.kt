package com.example.demoapp.app.user

import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/users")
class StandardUserController(
        private val userRegistrationWorkflow: UserRegistrationWorkflow
) {

    @PostMapping("/begin-registration")
    fun beginRegistration(@RequestBody @Valid dto: UserRegistrationDto) =
            userRegistrationWorkflow.beginRegistration(dto)

    @PostMapping("/complete-registration/{uuid}")
    fun completeRegistration(@PathVariable uuid: UUID) =
            userRegistrationWorkflow.completeRegistration(uuid)
                    .map { user -> UserDto(user.id, user.name) }
}
