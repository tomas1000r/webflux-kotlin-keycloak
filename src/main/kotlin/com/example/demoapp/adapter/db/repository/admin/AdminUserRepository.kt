package com.example.demoapp.adapter.db.repository.admin

import com.example.demoapp.adapter.db.entity.User
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository

@Repository
interface AdminUserRepository : R2dbcRepository<User, Long>
