# 0014 — Documentación OpenAPI con springdoc, protegida

- Estado: Aceptado
- Fecha: 2026-11-16

## Contexto

Quien integra un cliente necesita conocer las operaciones, sus datos y sus errores sin leer el código.

## Decisión

- springdoc-openapi genera el contrato desde los controladores. Se sirve en `/v3/api-docs` y Swagger UI en `/swagger-ui/index.html`.
- La documentación pide la credencial de la API, también en producción, como todo lo demás ([ADR 0006](0006-seguridad-http-basic-sin-estado.md)).
- El contrato declara HTTP Basic para todas las operaciones. Las públicas lo anulan con `@SecurityRequirements` y el estado de salud, en `OpenApiConfiguration`.
- Las operaciones de Actuator expuestas también aparecen (`springdoc.show-actuator`).
- Las anotaciones de cada controlador van en una interfaz `<Modulo>Api` (`AuthApi`). Cada error lleva un ejemplo por motivo, construido con los mensajes del `@RestControllerAdvice`.
- Cada DTO documenta sus límites y ejemplos con `@Schema`.

## Consecuencias

- Un endpoint nuevo aparece solo, pero sus errores y límites hay que anotarlos a mano.
- Los mensajes de Spring (`Invalid request content.`) se copian en los ejemplos: si Spring los cambia, el ejemplo queda desfasado.
- Abrir un endpoint en una `SecurityFilterChain` exige también marcarlo público en el contrato. Los escenarios de `openapi/` lo comprueban.
- El navegador pide la credencial al abrir Swagger UI y la reutiliza en **Try it out**.

## Dónde verlo

`src/main/java/com/crockgold/core/openapi/`, `src/main/java/com/crockgold/core/auth/controllers/` y `src/test/resources/features/openapi/`.
