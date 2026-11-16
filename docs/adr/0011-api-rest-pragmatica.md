# 0011 — API REST pragmática, no REST completo

- Estado: Aceptado
- Fecha: 2026-11-13

## Contexto

Seguir REST al pie de la letra (HATEOAS, todo como recurso) añade código y formato que ningún cliente usa.

## Decisión

- JSON sobre HTTP con verbos y códigos de estado estándar.
- Sin HATEOAS ni enlaces en las respuestas.
- Se permiten endpoints de acción cuando el recurso no encaja, como `POST /auth/login`.
- Los errores se devuelven como `ProblemDetail` (RFC 9457), activado con `spring.mvc.problemdetails.enabled`.

## Consecuencias

- Los errores de validación y de JSON mal formado responden `400` con cuerpo, sin pasar por `/error`.
- Cada módulo traduce sus excepciones a `ProblemDetail` en su propio `@RestControllerAdvice`.
- Un `201` no exige cabecera `Location`.

## Dónde verlo

`src/main/resources/application.yaml` y `src/main/java/com/crockgold/core/auth/controllers/`.
