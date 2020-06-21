package com.example.demoapp

import com.ninjasquad.springmockk.MockkBean
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository

@SpringBootTest
class DemoAppApplicationTests {

    @MockkBean
    private lateinit var clientRegistrations: ReactiveClientRegistrationRepository;

    @Test
    fun contextLoads() {
    }
}
