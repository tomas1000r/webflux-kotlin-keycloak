import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/* ============================================================================
     Repositories
   ============================================================================ */

repositories {
    mavenCentral()
}

/* ============================================================================
     Plugins
   ============================================================================ */

plugins {
    id("org.springframework.boot") version "2.3.0.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("com.avast.gradle.docker-compose") version "0.12.1"
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.spring") version "1.3.72"
    kotlin("plugin.jpa") version "1.3.72"
    kotlin("plugin.allopen") version "1.3.72"
}

/* ============================================================================
     Project information
   ============================================================================ */

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

/* ============================================================================
     Source sets
   ============================================================================ */

sourceSets {
    create("it") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

/* ============================================================================
     Configurations
   ============================================================================ */

val itImplementation by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

val itRuntimeOnly by configurations.getting {
    extendsFrom(configurations.runtimeOnly.get())
}

/* ============================================================================
     Dependencies
   ============================================================================ */

dependencies {

    // Implementation

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    implementation("org.springframework.security:spring-security-config")
    implementation("org.springframework.security:spring-security-oauth2-client")
    implementation("org.springframework.security:spring-security-oauth2-jose")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("dev.miku:r2dbc-mysql")
    implementation("org.springdoc:springdoc-openapi-webflux-ui:1.2.32")
    implementation("com.squareup.moshi:moshi:1.8.0")
    implementation("org.keycloak:keycloak-admin-client:10.0.2")

    // RuntimeOnly

    runtimeOnly("mysql:mysql-connector-java")

    // AnnotationProcessor

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // TestImplementation

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "junit")
        exclude(module = "junit-vintage-engine")
        exclude(module = "mockito-core")
    }

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("com.ninja-squad:springmockk:2.0.1")
    testImplementation("io.projectreactor:reactor-test")

    // TestRuntimeOnly

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    // ItImplementation

    itImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "junit")
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(module = "mockito-core")
    }

    itImplementation("org.junit.jupiter:junit-jupiter-api")

    // ItRuntimeOnly

    itRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

}

/* ============================================================================
     Tasks configuration
   ============================================================================ */

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

/* ============================================================================
     Tasks
   ============================================================================ */

val integrationTest = task<Test>("integrationTest") {
    description = "Runs integration tests."
    group = "verification"

    testClassesDirs = sourceSets["it"].output.classesDirs
    classpath = sourceSets["it"].runtimeClasspath

    shouldRunAfter("test")
}

tasks.check { dependsOn(integrationTest) }

/* ============================================================================
     Plugin configuration
   ============================================================================ */

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}

dockerCompose {
    useComposeFiles = listOf("./src/main/docker/docker-compose.yml")
    projectName = "demo-app"
    isRequiredBy(integrationTest)
}
