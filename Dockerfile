# syntax=docker/dockerfile:1

# ----- dev: entorno de desarrollo (Dev Container / compose) -----
FROM eclipse-temurin:25-jdk AS dev
RUN apt-get update \
    && apt-get install -y --no-install-recommends git curl postgresql-client \
    && rm -rf /var/lib/apt/lists/*
USER ubuntu
# Los volúmenes montados aquí heredan el dueño del directorio
RUN mkdir -p /home/ubuntu/.gradle /home/ubuntu/.claude
WORKDIR /app
CMD ["sleep", "infinity"]

# ----- build: empaquetado del JAR -----
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app
COPY gradlew settings.gradle build.gradle gradle.properties ./
COPY gradle gradle
COPY src src
RUN --mount=type=cache,target=/root/.gradle ./gradlew --no-daemon bootJar

# ----- prod: imagen final mínima (solo JRE, no-root) -----
FROM eclipse-temurin:25-jre AS prod
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
USER ubuntu
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
