package com.example.demoapp.adapter.cache.repository

import com.example.demoapp.adapter.cache.entity.RegistrationLink
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.*

@Repository
class RegistrationLinkRepository(
        private val operations: ReactiveRedisOperations<String, RegistrationLink>
) {

    fun create(link: RegistrationLink): Mono<RegistrationLink> {
        return operations.opsForSet()
                .add(link.uuid, link)
                .then(operations.expire(link.uuid, Duration.ofMinutes(30)))
                .thenReturn(link)
    }

    fun findByUuid(uuid: UUID): Mono<RegistrationLink> {
        return operations.opsForSet()
                .pop(uuid.toString())
    }
}