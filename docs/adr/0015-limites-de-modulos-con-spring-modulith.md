# 0015 — Límites entre módulos verificados con Spring Modulith

- Estado: Aceptado
- Fecha: 2026-11-16

## Contexto

El [ADR 0012](0012-hexagonal-no-estricta.md) separa cada módulo en un paquete de primer nivel, pero nada impide que un módulo use las clases internas de otro.

## Decisión

- Spring Modulith 2.1 (BOM `spring-modulith-bom`) con `spring-modulith-starter-core` y `spring-modulith-starter-test`.
- Cada paquete de primer nivel de `com.crockgold.core` es un módulo. Su API pública es solo el paquete raíz del módulo. Los subpaquetes (`services`, `domain`, ...) son internos.
- `ModularityTests` ejecuta `ApplicationModules.verify()` dentro de `./gradlew check` ([ADR 0003](0003-calidad-en-gradle-check.md)).

## Consecuencias

- Un módulo que use un tipo interno de otro (por ejemplo, `openapi` usando `auth.services.AuthService`) o un ciclo entre módulos hace fallar el check.
- Para que otro módulo use un tipo, se mueve al paquete raíz del módulo o se expone con `@NamedInterface`.
- El test no levanta Spring ni base de datos: analiza el bytecode.

## Dónde verlo

`build.gradle` y `src/test/java/com/crockgold/core/ModularityTests.java`.
