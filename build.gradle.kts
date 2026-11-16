plugins {
    java
    id("org.springframework.boot") version "4.1.2"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.quiz"
version = "0.0.1-SNAPSHOT"
description = "quiz"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("software.amazon.awssdk:s3:2.56.0")
    implementation("software.amazon.awssdk:dsql:2.31.32")
    implementation("com.zaxxer:HikariCP:7.1.0")
    implementation("com.amazonaws:aws-lambda-java-core:1.4.1")
    implementation("com.amazonaws:aws-lambda-java-events:3.16.1")
    implementation("com.amazonaws:aws-lambda-java-serialization:1.4.1")
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
