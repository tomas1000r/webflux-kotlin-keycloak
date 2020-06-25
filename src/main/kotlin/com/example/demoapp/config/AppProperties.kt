package com.example.demoapp.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("app")
data class AppProperties(
        val baseUrl: String,
        val completeRegistrationUrl: String,
        val keycloakClientId: String,
        val keycloakClientSecret: String,
        val keycloakIssuerUri: String
)
