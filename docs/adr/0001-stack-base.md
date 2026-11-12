# 0001 — Java 25, Spring Boot y Gradle como base

- Estado: Aceptado
- Fecha: 2026-11-10

## Contexto

Crockgold Core es un servicio backend que expone una API HTTP.
Necesita una base conocida sobre la que construir el resto.

## Decisión

- El proyecto es una aplicación **Spring Boot 4.1.2** escrita en **Java 25**.
- Se construye con **Gradle**, usando el wrapper `gradlew`, Gradle 9.9.0.
- La API se sirve con Spring Web MVC, junto a Validation, Actuator y Lombok.
- La caché de configuración de Gradle está activada.

## Consecuencias

- Solo hace falta un JDK 25, el wrapper descarga la versión correcta de Gradle.
- Las versiones de las libs de Spring las gestiona el plugin de Spring Boot.
- Lombok y Mockito necesitan ajustes extra en JDK 25, que ya están en `build.gradle`.

## Dónde verlo

- `build.gradle`
- `gradle.properties`
- `gradle/wrapper/gradle-wrapper.properties`
