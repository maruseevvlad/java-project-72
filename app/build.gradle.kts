plugins {
    application
    jacoco
    id("java")
    id("checkstyle")
    id("io.freefair.lombok") version "8.13.1"
    id("org.sonarqube") version "6.3.1.5724"
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

application {
    mainClass.value("hexlet.code.App")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(platform("org.junit:junit-bom:5.11.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("info.picocli:picocli:4.7.6")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.17.2")
    implementation("io.javalin:javalin:6.7.0")
    implementation("org.slf4j:slf4j-simple:2.0.17")
    implementation("com.zaxxer:HikariCP:7.0.2")
    implementation ("com.h2database:h2:2.2.224")
    implementation ("org.postgresql:postgresql:42.7.4")
    implementation("gg.jte:jte:3.2.1")
    implementation("io.javalin:javalin-bundle:6.7.0")
    implementation("io.javalin:javalin-rendering:6.7.0")

    implementation ("gg.jte:jte:3.1.9")
    implementation ("gg.jte:jte-runtime:3.1.9")
}

checkstyle {
    // Укажите путь к файлу конфигурации
    configFile = File(rootDir, "./config/checkstyle/checkstyle.xml")
}

sonar {
    properties {
        property ("sonar.projectKey", "maruseevvlad_java-project-72")
        property ("sonar.organization", "maruseevvlad")
        property ("sonar.host.url", "https://sonarcloud.io")
        property("sonar.verbose", "true") // Включить подробный вывод
        property("sonar.scanner.dumpToFile", "true") // Сохранить лог в файл
        property("sonar.login", System.getenv("SONAR_TOKEN"))
    }
}

tasks.test {
    useJUnitPlatform()
}