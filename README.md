# Demo Application in Kotlin + Spring Webflux + Keycloak

## Motivation

I'd like to demonstrate how to write & configure simple backend application utilizing modern technology stack like Kotlin and Webflux. It can be used as a starter template for microservice in enterprise app or backend for mobile app.

The project configuration is designed with the focus on simplicity and easy-to-run local development environment.

#### Used technologies

- R2DBC - reactive access to relational database
- Redis - store registration tokens
- MySQL - store user information
- Keycloak - authorization server and idp

#### User registration feature

The user registration process is divided in two steps:
1. First, user have to enter the data including email address. The registration link with validity of 30 minutes is sent to the user email address.
2. Second, when user clicks the link, the registration process is finished

#### Method level security

To use method level security, some configuration had to be done in SecurityConfiguration class.
Please note ReactiveOAuth2UserService bean where custom mapping between Keycloak roles and authorities had to be configured.

#### Custom mapping of roles and authorities

Class `OidcAuthorizationCodeReactiveAuthenticationManager` loads the `OidcUser` and tries to map received authorities to `GrantedAuthority` objects.
The Keycloak provides the Realm roles in 'attributes' map and not directly 'authorities' property. Therefore the custom mapping needed to be provided.
To use method level security expression like `@PreAuthorize("hasRole('ROLE_CUSTOM')")`, the manual mapping has to be implemented.
See `SecurityConfiguration.kt` and `ReactiveOAuth2UserService` bean, or https://github.com/spring-projects/spring-security/issues/6266

## Build

To build the project including running all the tests, just execute this command in the root of the project folder:

`./gradlew build`

## Local Development

Local development should be especially easy just by executing:

1. `./gradlew composeUp` command and the required components are up and configured
2. Run DemoAppApplication.kt from your IDE
3. To ensure the app is working, open `http://localhost:8080` in your browser
4. You can open Swagger documentation which is not protected or you can access protected resource
5. To view protected resource, enter credentials 'user' as login name and 'password' for password
6. To register new user, click 'Register new user' button

Note: MySQL data and Keycloak realm is imported automatically after containers starts.

Docker compose file contains 'convenient' containers for displaying the data, for example:

- `adminer` - View MySQL data
- `redis-commander` - View redis keys and values

## Shutdown local environment

To shutdown the local environment just run `./gradlew composeDown`

## Integration Tests

To run integration tests, just run `./gradlew integrationTest` task. The tests are in `src/it/kotlin` folder

## Export Keycloak Realm

To export the changes made in existing realm, execute the following command:

```
docker exec -it demo-app_keycloak_1 /opt/jboss/keycloak/bin/standalone.sh \
-Djboss.socket.binding.port-offset=100 -Dkeycloak.migration.action=export \
-Dkeycloak.migration.provider=singleFile \
-Dkeycloak.migration.realmName=demo \
-Dkeycloak.migration.usersExportStrategy=REALM_FILE \
-Dkeycloak.migration.file=/tmp/demo-realm.json
```

Please note, that when Keycloak container starts, the demo-realm.json is automatically imported. This is configured in docker-compose.yml.

## Keycloak

To access Keycloak admin console, open `http://localhost:7080` page and use `admin` user with password `admin`.

## TODO

* Deliver this demo application as docker image
* Support both options for running local dev stack:
  - running demo application as a docker container within docker-compose.yml
  - running demo application from IDE by running `DemoAppApplication.kt` class
* Implement missing unit and integration tests
* Implement supporting admin UI
* Implement demo client app
* User registration via Keycloak

## Resources:

#### Keycloak Container

https://github.com/keycloak/keycloak-containers/blob/10.0.2/server/README.md

#### Spring Boot + Kotlin

https://spring.io/guides/tutorials/spring-boot-kotlin

#### Docker-Compose Gradle Plugin

https://bmuschko.com/blog/gradle-docker-compose

#### R2DBC

https://r2dbc.io

#### Spring Boot Keycloak Security

https://www.thomasvitale.com/spring-boot-keycloak-security/

https://docs.spring.io/spring-security/site/docs/current/reference/html5/#webflux-oauth2

https://github.com/spring-projects/spring-security/tree/master/samples/boot/oauth2login-webflux

#### No authorization server support in Spring OAuth

https://spring.io/blog/2019/11/14/spring-security-oauth-2-0-roadmap-update
