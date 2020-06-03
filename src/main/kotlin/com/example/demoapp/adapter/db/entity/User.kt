package com.example.demoapp.adapter.db.entity

import org.springframework.data.annotation.Id
import javax.validation.constraints.Size

class User(
        @Id
        var id: Long?,

        @get:Size(min = 5, max = 20)
        val name: String
)
