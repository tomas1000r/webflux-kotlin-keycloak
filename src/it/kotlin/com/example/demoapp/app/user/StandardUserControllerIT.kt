package com.example.demoapp.app.user

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StandardUserControllerIT(@Autowired val restTemplate: TestRestTemplate) {

    @BeforeEach
    fun before() {
        println("Setup...")
    }

    @Test
    fun shouldBeginRegistration() {
        val payload = UserRegistrationDto("first", "last", "test@email.com")
        val response = restTemplate.postForEntity<String>("/api/users/begin-registration", payload)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertTrue(response.body?.startsWith("http://localhost:8080/api/users/complete-registration/")!!)
    }
}
