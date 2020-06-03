package com.example.demoapp.app.user

import com.example.demoapp.adapter.cache.entity.RegistrationLink
import com.example.demoapp.adapter.cache.repository.RegistrationLinkRepository
import com.example.demoapp.adapter.db.entity.User
import com.example.demoapp.adapter.db.repository.standard.StandardUserRepository
import com.example.demoapp.app.config.ApplicationProperties
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.*

@Component
class UserRegistrationWorkflow(
        private val properties: ApplicationProperties,
        private val registrationLinkRepository: RegistrationLinkRepository,
        private val standardUserRepository: StandardUserRepository
) {

    fun beginRegistration(dto: UserRegistrationDto) =
            Mono.just(UUID.randomUUID().toString())
                    .flatMap { uuid ->
                        registrationLinkRepository
                                .create(RegistrationLink(uuid, dto.email))
                                .map { link -> properties.completeRegistrationUrl + "/" + link.uuid }
                    }

    fun completeRegistration(uuid: UUID) =
            registrationLinkRepository.findByUuid(uuid)
                    .flatMap { link -> standardUserRepository.save(User(null, link.email)) }
}
