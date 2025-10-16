FROM eclipse-temurin:21-jdk

ARG GRADLE_VERSION=8.10

WORKDIR /app

COPY app/ .

RUN ./gradlew installDist

CMD ./build/install/app/bin/app