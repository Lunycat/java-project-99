plugins {
    application
    jacoco
    checkstyle
    id("io.freefair.lombok") version "8.6"
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.3"
    id("io.sentry.jvm.gradle") version "4.14.1"
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

    implementation("org.openapitools:jackson-databind-nullable:0.2.6")

    implementation("org.mapstruct:mapstruct:1.6.0.Beta1")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.0.Beta1")

    implementation("org.instancio:instancio-junit:3.6.0")
    implementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.2")

    implementation("net.datafaker:datafaker:2.0.2")

    runtimeOnly("com.h2database:h2:2.2.224")

    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application { mainClass.set("io.hexlet.blog.Application") }

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.jacocoTestReport { reports { xml.required.set(true) } }

tasks.withType<Test> {
    useJUnitPlatform()
}

sentry {
    // Generates a JVM (Java, Kotlin, etc.) source bundle and uploads your source code to Sentry.
    // This enables source context, allowing you to see your source
    // code as part of your stack traces in Sentry.
    includeSourceContext = true

    org = "hexlet-0b"
    projectName = "java"
    authToken = System.getenv("SENTRY_AUTH_TOKEN")
}