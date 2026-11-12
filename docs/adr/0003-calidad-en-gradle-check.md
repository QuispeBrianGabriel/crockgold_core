# 0003 — Toda la calidad de código pasa por `./gradlew check`

- Estado: Aceptado
- Fecha: 2026-11-10

## Contexto

Se requiere que una sola orden indique y valide la calidad del código, que funcione igual para cada Dev, facilitando la integración continua.

## Decisión

`./gradlew check` reúne todas las comprobaciones:

- **Formato**: Spotless con Palantir Java Format.
- **Análisis estático**: Error Prone, durante la compilación.
- **Tests**: JUnit.
- **Cobertura**: JaCoCo mide y el plugin `jacocolog` imprime el resumen en la consola al terminar los tests (sin informe HTML).

**Extra**: `.editorconfig` fija lo básico para cualquier editor.

## Consecuencias

- El pipeline solo ejecuta `./gradlew check`.
- El código no formateado falla, se arregla con `./gradlew spotlessApply`.
- El editor no formatea al guardar, el único formato válido es Spotless.

## Dónde verlo

- `build.gradle`
- `.editorconfig`
- `.devcontainer/devcontainer.json`
- `.github/workflows/pipeline.yml`
