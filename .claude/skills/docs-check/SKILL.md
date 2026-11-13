---
name: docs-check
description: Valida que la documentación diga la verdad. Contrasta docs/ con el código y la configuración del repositorio e informa de errores, incoherencias y contenido desactualizado, sin modificar nada. Usar cuando se pida revisar, validar o auditar la documentación.
argument-hint: "[documentos, vacío = todos]"
allowed-tools: Bash(scripts/docs-lint.sh)
disallowed-tools: Edit Write NotebookEdit
---

Alcance: $ARGUMENTS

Sin alcance, se revisan `README.md` y todo `docs/`.

## Lint mecánico (ya ejecutado)

!`scripts/docs-lint.sh`

Si arriba no hay salida, ejecuta `scripts/docs-lint.sh`. No repitas a mano lo que el lint ya comprueba: enlaces, rutas, tareas de `make`, índices y formato.

## Verificación

Por cada documento del alcance:

1. Extrae las afirmaciones comprobables: comandos y parámetros, variables y valores, versiones, puertos, nombres (jobs, servicios, volúmenes, imágenes), mensajes de log y comportamiento descrito.
2. Contrasta cada una con su fuente usando Grep o leyendo solo el fragmento necesario.
3. Busca contradicciones entre documentos y datos repetidos en más de uno.
4. Revisa las reglas de `.claude/rules/docs.md` que el lint no cubre: redundancia, párrafos largos y bloques de código extensos.

| Tema | Fuente |
| ---- | ------ |
| Tareas y parámetros | `Makefile`, `scripts/` |
| Variables, puertos, volúmenes | `docker/compose.yml`, `docker/.env.example`, `src/main/resources/` |
| Versiones | `build.gradle`, `gradle/wrapper/gradle-wrapper.properties`, `docker/Dockerfile.*` |
| Pipeline y release | `.github/workflows/` |
| Comportamiento de la API | `src/` |

Lo que vive fuera del repositorio (Railway) no se puede comprobar: se lista aparte, sin darlo por bueno ni por malo.

## Informe

Solo hallazgos, ordenados por gravedad. No listes lo que está bien.

| Documento:línea | Dice | Realidad | Fuente |
| --------------- | ---- | -------- | ------ |

Después, la lista **No verificable** y una línea de resumen con documentos revisados y hallazgos.

Límites:

- No modifiques nada. Termina preguntando si se aplican las correcciones.
- El único comando permitido es `scripts/docs-lint.sh`.
