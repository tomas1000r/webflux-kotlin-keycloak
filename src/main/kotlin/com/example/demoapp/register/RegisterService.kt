package com.example.demoapp.register

import com.example.demoapp.adapter.cache.entity.RegistrationLink
import com.example.demoapp.adapter.cache.repository.RegistrationLinkRepository
import com.example.demoapp.adapter.db.entity.User
import com.example.demoapp.adapter.db.repository.standard.StandardUserRepository
import com.example.demoapp.config.AppProperties
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.*

@Component
class RegisterService(
        private val properties: AppProperties,
        private val registrationLinkRepository: RegistrationLinkRepository,
        private val standardUserRepository: StandardUserRepository
) {

    fun beginRegistration(dto: RegisterDto): Mono<String> {
        return Mono.just(UUID.randomUUID().toString())
                .flatMap { uuid ->
                    registrationLinkRepository
                            .create(RegistrationLink(uuid, dto.email))
                            .map { link -> properties.completeRegistrationUrl + "/" + link.uuid }
                }
    }

    fun completeRegistration(uuid: UUID): Mono<User> {
        return registrationLinkRepository.findByUuid(uuid)
                .flatMap { link -> standardUserRepository.save(User(null, link.email)) }
    }
}
