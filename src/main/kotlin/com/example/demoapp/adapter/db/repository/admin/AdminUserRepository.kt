package com.example.demoapp.adapter.db.repository.admin

import com.example.demoapp.adapter.db.entity.User
import org.springframework.data.r2dbc.repository.R2dbcRepository

interface AdminUserRepository : R2dbcRepository<User, Long>
