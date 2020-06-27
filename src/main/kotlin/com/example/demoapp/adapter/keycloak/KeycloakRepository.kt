package com.example.demoapp.adapter.keycloak

import org.keycloak.admin.client.Keycloak
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class KeycloakRepository(
        private val properties: KeycloakProperties,
        private val keycloak: Keycloak
) {

    fun createUser(user: KeycloakUser): Mono<KeycloakUser> {
        val password = user.password ?: generatePassword()

        val credentialRepresentation = CredentialRepresentation()
        credentialRepresentation.type = CredentialRepresentation.PASSWORD
        credentialRepresentation.value = password

        val userRepresentation = UserRepresentation()
        userRepresentation.username = user.email
        userRepresentation.firstName = user.firstName
        userRepresentation.lastName = user.lastName
        userRepresentation.isEnabled = true
        userRepresentation.credentials = listOf(credentialRepresentation)

        keycloak
                .realm(properties.realm)
                .users()
                .create(userRepresentation);

        return Mono.just(KeycloakUser(user.firstName, user.lastName, user.email, password))
    }

    fun generatePassword(): String {
        return "1234"
    }
}
