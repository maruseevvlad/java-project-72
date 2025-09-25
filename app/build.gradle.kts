plugins {
    application
    java
    jacoco
    checkstyle
    id("io.freefair.lombok") version "8.13.1"
    id("org.sonarqube") version "6.3.1.5724"
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("hexlet.code.App")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.3")

    implementation("info.picocli:picocli:4.7.6")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.17.2")
    implementation("org.slf4j:slf4j-simple:2.0.17")
    implementation("com.zaxxer:HikariCP:7.0.2")
    implementation("com.h2database:h2:2.2.224")
    implementation("org.postgresql:postgresql:42.7.4")

    implementation("io.javalin:javalin:6.7.0")
    implementation("io.javalin:javalin-bundle:6.7.0")
    implementation("io.javalin:javalin-rendering:6.7.0")

    implementation("gg.jte:jte:3.2.1")
    implementation("gg.jte:jte-runtime:3.2.1")
}

checkstyle {
    configFile = File(rootDir, "./config/checkstyle/checkstyle.xml")
}

tasks.test {
    useJUnitPlatform()
    // Включаем отчёт для Jacoco
    finalizedBy("jacocoTestReport")
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // тесты должны выполниться перед отчётом
    reports {
        xml.required.set(true)   // важно для SonarQube
        html.required.set(true)
        csv.required.set(false)
    }
}

sonar {
    properties {
        property("sonar.projectKey", "maruseevvlad_java-project-72")
        property("sonar.organization", "maruseevvlad")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.login", System.getenv("SONAR_TOKEN"))
        property("sonar.verbose", "true")
        property("sonar.scanner.dumpToFile", "true")
        // Указываем путь к xml отчёту Jacoco
        property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/test/jacocoTestReport.xml")
    }
}
