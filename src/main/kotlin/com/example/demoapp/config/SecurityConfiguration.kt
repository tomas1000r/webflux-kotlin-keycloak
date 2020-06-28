package com.example.demoapp.config

import com.example.demoapp.adapter.keycloak.KeycloakProperties
import net.minidev.json.JSONArray
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.oidc.userinfo.OidcReactiveOAuth2UserService
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.registration.ClientRegistrations
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.web.server.SecurityWebFilterChain


@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
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
                .pathMatchers("/register.html").permitAll()
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
    fun clientRegistrations(properties: KeycloakProperties): ReactiveClientRegistrationRepository {
        val clientRegistration = ClientRegistrations
                .fromIssuerLocation(properties.issuerUrl)
                .clientId(properties.clientId)
                .clientSecret(properties.clientSecret)
                .build()

        return InMemoryReactiveClientRegistrationRepository(clientRegistration)
    }

    @Bean
    fun oidcUserService(): ReactiveOAuth2UserService<OidcUserRequest, OidcUser> {
        val delegate = OidcReactiveOAuth2UserService()
        return ReactiveOAuth2UserService { userRequest ->
            val oidcUser = delegate.loadUser(userRequest)
                    .map { oidcUser ->
                        val mappedAuthorities = mutableSetOf<GrantedAuthority>()
                        mappedAuthorities.addAll(oidcUser.authorities)

                        // Map keycloak roles to authorities
                        (oidcUser.attributes["groups"] as JSONArray)
                                .forEach { t: Any? ->
                                    run {
                                        val roleName = "ROLE_" + (t as String?)?.toUpperCase()
                                        mappedAuthorities.add(SimpleGrantedAuthority(roleName))
                                    }
                                }

                        DefaultOidcUser(mappedAuthorities, oidcUser.idToken, oidcUser.userInfo) as OidcUser
                    }
            oidcUser
        }
    }
}
