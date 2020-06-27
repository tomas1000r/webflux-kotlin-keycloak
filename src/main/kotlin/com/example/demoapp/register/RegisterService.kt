package com.example.demoapp.register

import com.example.demoapp.adapter.cache.RegistrationMetadata
import com.example.demoapp.adapter.cache.RegistrationMetadataRepository
import com.example.demoapp.adapter.db.entity.User
import com.example.demoapp.adapter.db.repository.standard.StandardUserRepository
import com.example.demoapp.adapter.keycloak.KeycloakRepository
import com.example.demoapp.adapter.keycloak.KeycloakUser
import com.example.demoapp.config.AppProperties
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class RegisterService(
        private val properties: AppProperties,
        private val registrationMetadataRepository: RegistrationMetadataRepository,
        private val standardUserRepository: StandardUserRepository,
        private val keycloakRepository: KeycloakRepository
) {

    fun beginRegistration(details: RegistrationDetails): Mono<String> {
        // TODO  Send confirmation link to email

        return Mono.just(UUID.randomUUID().toString())
                .flatMap { uuid ->
                    registrationMetadataRepository
                            .create(RegistrationMetadata(uuid, details.email, details.firstName, details.lastName, details.password))
                            .map { metadata -> properties.confirmRegistrationUrl + "/" + metadata.uuid }
                }
    }

    fun confirmRegistration(uuid: UUID): Mono<Void> {
        // TODO send generated password to email

        return registrationMetadataRepository.findByUuid(uuid)
                .doOnNext { metadata ->
                    run {
                        standardUserRepository.save(User(null, metadata.email))

                        keycloakRepository.createUser(KeycloakUser(metadata.firstName, metadata.lastName, metadata.email, metadata.password))
                    }
                }.then()
    }
}
