package com.example.demoapp.adapter.db.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("adapter.db")
data class DatabaseProperties(
        val host: String,
        val port: Int,
        val dbName: String,
        val adminUsername: String,
        val adminPassword: String,
        val standardUsername: String,
        val standardPassword: String
)
