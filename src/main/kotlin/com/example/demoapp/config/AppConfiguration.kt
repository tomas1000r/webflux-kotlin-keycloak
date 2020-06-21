package com.example.demoapp.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.http.MediaType.TEXT_HTML
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions.resources
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
@EnableConfigurationProperties(AppProperties::class)
class AppConfiguration {

    @Bean
    fun staticResourcesRouter(): RouterFunction<ServerResponse> {
        return resources("/**", ClassPathResource("static/"))
    }

    @Bean
    fun indexRouter(@Value("classpath:/static/index.html") html: Resource): RouterFunction<ServerResponse> {
        return router {
            GET("/") {
                ok().contentType(TEXT_HTML).bodyValue(html)
            }
        }
    }
}
