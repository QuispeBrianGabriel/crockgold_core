# syntax=docker/dockerfile:1

# ----- base: JDK 25 + herramientas comunes -----
FROM eclipse-temurin:25-jdk AS base
RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        git \
        curl \
        ca-certificates \
        postgresql-client \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*
WORKDIR /app

# ----- dev: entorno de desarrollo (Dev Container / compose) -----
FROM base AS dev
ENV GRADLE_USER_HOME=/home/ubuntu/.gradle
RUN mkdir -p "$GRADLE_USER_HOME" /app \
    && chown -R ubuntu:ubuntu \
        "$GRADLE_USER_HOME" \
        /app
USER ubuntu
EXPOSE 8080
CMD ["sleep", "infinity"]

# ----- build: empaquetado del JAR -----
FROM base AS build
COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew --no-daemon dependencies > /dev/null
COPY src src
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew --no-daemon bootJar -x test \
    && cp build/libs/*[!n].jar /app/app.jar

# ----- prod: imagen final mínima (solo JRE, no-root) -----
FROM eclipse-temurin:25-jre AS prod
WORKDIR /app
COPY --from=build /app/app.jar app.jar
USER ubuntu
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
