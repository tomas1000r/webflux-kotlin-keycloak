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

## Local Development

1. To prepare the stack, run the following command:
```
./local-stack-start.sh
```

2. When containers are ready, log in to Keycloak admin console using these credentials:
```
user: admin
password: admin
```

3. Add new realm by importing json file from:
```
keycloak/demo-realm.json
```

4. Import groups from json file
```
keycloak/demo-groups.json
```

## Shutdown Local Stack

When finished local development, you can shutdown the local stack by executing this command:

```
./local-stack-stop.sh
```

## Links:

https://spring.io/guides/tutorials/spring-boot-kotlin

https://bmuschko.com/blog/gradle-docker-compose

https://r2dbc.io

https://www.thomasvitale.com/spring-boot-keycloak-security/

https://docs.spring.io/spring-security/site/docs/current/reference/html5/#webflux-oauth2

https://github.com/spring-projects/spring-security/tree/master/samples/boot/oauth2login-webflux

No authorization server support

https://spring.io/blog/2019/11/14/spring-security-oauth-2-0-roadmap-update
