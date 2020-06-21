package com.example.demoapp.adapter.db.config

import com.example.demoapp.adapter.db.repository.admin.AdminUserRepository
import com.example.demoapp.adapter.db.repository.standard.StandardUserRepository
import dev.miku.r2dbc.mysql.MySqlConnectionConfiguration
import dev.miku.r2dbc.mysql.MySqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.DefaultReactiveDataAccessStrategy
import org.springframework.data.r2dbc.core.ReactiveDataAccessStrategy
import org.springframework.data.r2dbc.dialect.MySqlDialect
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@Configuration
@EnableConfigurationProperties(DatabaseProperties::class)
class DatabaseConfiguration {

    @Bean
    fun reactiveDataAccessStrategy(): ReactiveDataAccessStrategy {
        return DefaultReactiveDataAccessStrategy(MySqlDialect())
    }

    @Configuration
    @EnableR2dbcRepositories(basePackageClasses = [AdminUserRepository::class], databaseClientRef = "adminDatabaseClient")
    class AdminDatabaseConfiguration(
            private val properties: DatabaseProperties
    ) {

        @Bean
        fun adminConnectionFactory(): ConnectionFactory {
            return MySqlConnectionFactory.from(MySqlConnectionConfiguration.builder()
                    .host(properties.host)
                    .port(properties.port)
                    .username(properties.adminUsername)
                    .password(properties.adminPassword)
                    .database(properties.dbName)
                    .build())
        }

        @Bean
        fun adminDatabaseClient(): DatabaseClient {
            return DatabaseClient.create(adminConnectionFactory())
        }
    }

    @Configuration
    @EnableR2dbcRepositories(basePackageClasses = [StandardUserRepository::class], databaseClientRef = "standardDatabaseClient")
    class StandardDatabaseConfiguration(
            private val properties: DatabaseProperties
    ) {

        @Bean
        fun standardConnectionFactory(): ConnectionFactory {
            return MySqlConnectionFactory.from(MySqlConnectionConfiguration.builder()
                    .host(properties.host)
                    .port(properties.port)
                    .username(properties.standardUsername)
                    .password(properties.standardPassword)
                    .database(properties.dbName)
                    .build())
        }

        @Bean("r2dbcDatabaseClient", "standardDatabaseClient")
        fun standardDatabaseClient(): DatabaseClient {
            return DatabaseClient.create(standardConnectionFactory())
        }
    }
}
