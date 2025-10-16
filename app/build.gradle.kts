plugins {
    id("java")
    id("org.sonarqube") version "6.2.0.5505"
    id("io.freefair.lombok") version "8.14"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    checkstyle
    jacoco
    application
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("gg.jte:jte:3.2.1")
    implementation("com.h2database:h2:2.2.224")
    implementation("org.postgresql:postgresql:42.7.1")
    implementation("io.javalin:javalin:6.7.0")
    implementation("io.javalin:javalin-rendering:6.7.0")
    implementation("io.javalin:javalin-bundle:6.7.0")
    implementation("org.slf4j:slf4j-simple:2.0.17")
    implementation("com.zaxxer:HikariCP:6.3.0")
    implementation("com.konghq:unirest-java:3.14.1")
    implementation("org.jsoup:jsoup:1.21.1")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.27.3")
    testImplementation("com.squareup.okhttp3:mockwebserver3:5.1.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
    }
}

application {
    mainClass = "hexlet.code.App"
}

sonar {
    properties {
        property("sonar.projectKey", "qusilon_java-project-72")
        property("sonar.organization", "qusilon")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}