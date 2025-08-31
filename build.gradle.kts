val jsonwebtokenVersion: String by project
val springdocOpenapiStarterVersion: String by project

plugins {
    id("java")
    id("org.springframework.boot") version "3.5.4"
    id("io.spring.dependency-management") version "1.1.7"

}

java {
    sourceCompatibility = JavaVersion.VERSION_23
}

group = "org.example.api"
version = "1.0-SNAPSHOT"



repositories {
    mavenCentral()
}


tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    //implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    //implementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo.spring3x:4.18.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springdocOpenapiStarterVersion")

    implementation("io.jsonwebtoken:jjwt-api:$jsonwebtokenVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jsonwebtokenVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jsonwebtokenVersion")

    runtimeOnly("org.postgresql:postgresql")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}
