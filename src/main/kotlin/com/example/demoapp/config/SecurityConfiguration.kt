package com.example.demoapp.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.registration.ClientRegistrations
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.security.web.server.SecurityWebFilterChain


@Configuration
@EnableWebFluxSecurity
class SecurityConfiguration {

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
                .authorizeExchange()
                .pathMatchers("/").permitAll()
                .pathMatchers("/swagger-ui.html").permitAll()
                .pathMatchers("/webjars/**").permitAll()
                .pathMatchers("/v3/**").permitAll()
                .pathMatchers("/actuator/**").permitAll()
                .pathMatchers("/api/register/**").permitAll()
                .anyExchange().authenticated()
                .and()
                .oauth2Login()
                .and()
                .csrf().disable()
                .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun clientRegistrations(): ReactiveClientRegistrationRepository {
        val clientRegistration = ClientRegistrations
                .fromIssuerLocation("http://localhost:7080/auth/realms/demo")
                .clientId("spring-security")
                .clientSecret("342a09be-808a-4b65-90d0-a79f2de45048")
                .build()

        return InMemoryReactiveClientRegistrationRepository(clientRegistration)
    }
}
