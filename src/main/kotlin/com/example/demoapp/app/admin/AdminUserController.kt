package com.example.demoapp.app.admin

import com.example.demoapp.adapter.db.repository.admin.AdminUserRepository
import com.example.demoapp.app.user.UserDto
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin/users")
class AdminUserController(
        private val adminUserRepository: AdminUserRepository
) {

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) =
            adminUserRepository.deleteById(id)

    @GetMapping
    fun findAll() = adminUserRepository
            .findAll()
            .map { u -> UserDto(u.id, u.name) }

}
