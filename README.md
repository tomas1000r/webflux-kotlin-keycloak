# Demo application project

## Motivation

In this small project I'd like to show how to setup Spring Boot 2 with Kotlin and R2DBC together and implement unit tests and integration tests.
The microservices are often connecting to other components like databases, messaging processors or other microservices.
To simplify the development process, it is great to have the local environment as docker containers up and running with single command.
When the containers are up, the application can be executed from the IDE and tested with Postman.
Also the integration tests can be run as part of the whole build or just single test.

## Build

To build the project including running all the tests, just execute this command in the root of the project folder:

`./gradlew build`



Links:

https://spring.io/guides/tutorials/spring-boot-kotlin

https://bmuschko.com/blog/gradle-docker-compose

https://r2dbc.io
