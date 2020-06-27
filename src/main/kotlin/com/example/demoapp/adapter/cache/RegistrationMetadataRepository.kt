package com.example.demoapp.adapter.cache

import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.*

@Repository
class RegistrationMetadataRepository(
        private val operations: ReactiveRedisOperations<String, RegistrationMetadata>
) {

    fun create(metadata: RegistrationMetadata): Mono<RegistrationMetadata> {
        return operations.opsForSet()
                .add(metadata.uuid, metadata)
                .then(operations.expire(metadata.uuid, Duration.ofMinutes(30)))
                .thenReturn(metadata)
    }

    fun findByUuid(uuid: UUID): Mono<RegistrationMetadata> {
        return operations.opsForSet()
                .pop(uuid.toString())
    }
}
