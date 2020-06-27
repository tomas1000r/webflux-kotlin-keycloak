package com.example.demoapp.adapter.keycloak

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("adapter.keycloak")
data class KeycloakProperties(
        val baseUrl: String,
        val issuerUrl: String,
        val clientId: String,
        val clientSecret: String,
        val realm: String,
        val adminClientId: String,
        val adminUsername: String,
        val adminPassword: String,
        val adminRealm: String
)
