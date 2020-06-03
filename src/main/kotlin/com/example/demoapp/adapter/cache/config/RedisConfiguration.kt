package com.example.demoapp.adapter.cache.config

import com.example.demoapp.adapter.cache.entity.RegistrationLink
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfiguration {

    @Bean
    fun redisOperations(factory: ReactiveRedisConnectionFactory): ReactiveRedisOperations<String, RegistrationLink> {
        val serializer = Jackson2JsonRedisSerializer(RegistrationLink::class.java)

        val builder: RedisSerializationContext.RedisSerializationContextBuilder<String, RegistrationLink> =
                RedisSerializationContext.newSerializationContext(StringRedisSerializer())

        val context = builder.value(serializer).build()

        return ReactiveRedisTemplate(factory, context)
    }
}
