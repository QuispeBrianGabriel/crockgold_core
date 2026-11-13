---
name: docs-write
description: Escribe o actualiza documentación en docs/ a partir de una descripción y del código del repositorio, aplicando las reglas del proyecto. Usar cuando se pida documentar algo o actualizar la documentación tras un cambio.
argument-hint: <qué documentar>
allowed-tools: Bash(scripts/docs-lint.sh)
---

Documentar: $ARGUMENTS

Sin argumentos, documenta lo trabajado en esta conversación. Si no hay nada que documentar, pregunta.

1. Lee `.claude/rules/docs.md` y `docs/README.md` si no están ya en contexto.
2. Decide dónde va según el índice. Amplía un documento existente antes de crear uno. Una decisión de arquitectura es un ADR nuevo.
3. Verifica cada dato en su fuente (`Makefile`, `scripts/`, `docker/`, `.github/workflows/`, `build.gradle`, `src/`) con Grep o leyendo solo el fragmento necesario. Lo que no puedas verificar se pregunta o va como **Por confirmar**.
4. Escribe lo mínimo que resuelve la necesidad. Borra lo que el cambio deje obsoleto o duplicado.
5. Si creaste un documento, añádelo a su índice (`docs/README.md` o `docs/adr/README.md`).
6. Ejecuta `scripts/docs-lint.sh` y corrige lo que marque.
7. Informa en 5 líneas como máximo: archivos tocados, líneas añadidas y quitadas, y lo que quedó **Por confirmar**.

Límites:

- Solo se modifican `docs/**` y `README.md`. Si el código está mal o falta una automatización, se informa y no se toca.
- El único comando permitido es `scripts/docs-lint.sh`.
