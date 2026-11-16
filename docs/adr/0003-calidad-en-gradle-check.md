# 0003 — Toda la calidad de código pasa por `./gradlew check`

- Estado: Aceptado
- Fecha: 2026-11-10

## Contexto

Hace falta una sola orden que valide la calidad del código y funcione igual para cada dev y en la integración continua.

## Decisión

`./gradlew check` reúne todas las comprobaciones:

- **Formato**: Spotless con el formateador de Eclipse (`config/eclipse-formatter.xml`): ancho 100 y `if` con cuerpo en línea aparte.
- **Análisis estático**: Error Prone, durante la compilación.
- **Tests**: JUnit y los escenarios de Cucumber ([ADR 0013](0013-integracion-con-cucumber-y-serenity.md)).
- **Cobertura**: JaCoCo mide y el plugin `jacocolog` imprime el resumen en la consola.

Además, `.editorconfig` fija lo básico para cualquier editor.

## Consecuencias

- El Pipeline solo ejecuta `./gradlew check`. En local es `make check`.
- El código sin formatear hace fallar el check y se arregla con `make format`.
- El editor no formatea Java al guardar: el único formato válido es Spotless.

## Dónde verlo

`build.gradle`, `.editorconfig`, `.devcontainer/devcontainer.json` y `.github/workflows/pipeline.yml`.
