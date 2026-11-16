# 0013 — Tests de integración con Cucumber y Serenity BDD

- Estado: Aceptado
- Fecha: 2026-11-15

## Contexto

Los tests de integración en JUnit mezclaban el comportamiento esperado con el código que lo comprueba. Cada clase arrancaba su propio contexto de Spring y su contenedor.

## Decisión

- Los tests de integración son escenarios Gherkin en `src/test/resources/features/<modulo>/`.
- Los ejecuta `IntegrationSuite` con Cucumber 8 y Serenity BDD 5, dentro de `./gradlew check`.
- Cada `.feature` abre con su historia de usuario (Como, Quiero, Para) y agrupa los escenarios por `Regla` de negocio.
- Los escenarios hablan el lenguaje del dominio. No nombran rutas, códigos de estado, JSON ni persistencia.
- Cada escenario tiene un único resultado: registro aceptado, o rechazo por un motivo concreto. Un motivo distinto es otro escenario. No se usa `Esquema del escenario`.
- Los valores que comparten resultado se pasan al escenario en una tabla de datos. Los pasos reciben los datos como parámetros para reutilizarse.
- Cada escenario lleva `@happy` o `@unhappy`, y `@border` si prueba un límite. Los marcados con `@wip` no se ejecutan ni hacen fallar `./gradlew check`.
- `integration/steps/` tiene una clase de pasos por módulo. Ella traduce el dominio a peticiones HTTP y comprueba las respuestas.
- `integration/support/` contiene el contexto de Spring, Testcontainers y el reinicio de la base.
- Todos los escenarios comparten un contexto de Spring y un contenedor `postgres:18`.
- Cada escenario empieza con la base recién migrada: `CleanDatabase` ejecuta `clean` y `migrate` de Flyway antes de cada uno.
- Los escenarios y los `@DisplayName` se escriben en español. El código, en inglés.
- Los tests unitarios siguen en JUnit, junto al paquete que prueban.

## Consecuencias

- Una tabla nueva no requiere tocar los tests: basta su migración.
- `clean` de Flyway solo se habilita en los tests, con `spring.flyway.clean-disabled=false` en `SpringContext`.
- Cambiar una ruta, un código de estado o un mensaje de la API toca los pasos, no los `.feature`.
- Lo que no tiene significado en el dominio, como un JSON mal formado, no se cubre con escenarios.
- Serenity deja su informe en `build/reports/serenity/`. La configuración vive en `src/test/resources/junit-platform.properties` y `serenity.properties`.

## Dónde verlo

`src/test/java/com/crockgold/core/integration/`, `src/test/resources/features/` y `build.gradle`.
