# 0001 — Java 25, Spring Boot y Gradle como base

- Estado: Aceptado
- Fecha: 2026-11-10

## Contexto

Crockgold Core es un servicio backend con una API HTTP y necesita una base conocida sobre la que construir.

## Decisión

- Aplicación **Spring Boot 4.1.2** en **Java 25**, con Web MVC, Validation, Actuator y Lombok.
- Se construye con **Gradle 9.9.0** mediante el wrapper `gradlew`, con la caché de configuración activada.

## Consecuencias

- Solo hace falta un JDK 25: el wrapper descarga Gradle.
- Las versiones de las librerías de Spring las gestiona el plugin de Spring Boot.
- Lombok y Mockito necesitan ajustes extra en JDK 25, que ya están en `build.gradle`.

## Dónde verlo

`build.gradle`, `gradle.properties` y `gradle/wrapper/gradle-wrapper.properties`.
