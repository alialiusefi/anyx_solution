plugins {
    kotlin("jvm") version "1.7.21"
    id("org.springframework.boot") version "2.7.7-SNAPSHOT"
    id("org.jetbrains.kotlin.plugin.spring") version "1.7.21"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.7.21"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    id("com.avast.gradle.docker-compose") version "0.16.11"
    id("org.flywaydb.flyway") version "9.8.3"
    application
}

apply(plugin = "io.spring.dependency-management")

group = "com.anymind"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-graphql")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.postgresql:postgresql:42.5.1")
    implementation("org.flywaydb:flyway-core:9.8.3")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

application {
    mainClass.set("com.anymind.sales.SalesApplicationKt")
}

dockerCompose {
    isRequiredBy(tasks.test)
    startedServices.set(listOf("db"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
