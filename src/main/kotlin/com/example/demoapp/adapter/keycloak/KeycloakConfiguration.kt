package com.example.demoapp.adapter.keycloak

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(KeycloakProperties::class)
class KeycloakConfiguration {

    @Bean
    fun keycloak(properties: KeycloakProperties): Keycloak {
        return KeycloakBuilder
                .builder()
                .serverUrl(properties.baseUrl)
                .realm(properties.adminRealm)
                .username(properties.adminUsername)
                .password(properties.adminPassword)
                .clientId(properties.adminClientId)
                .resteasyClient(ResteasyClientBuilder().connectionPoolSize(10).build())
                .build();
    }
}
