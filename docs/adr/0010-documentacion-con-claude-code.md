# 0010 — Documentación mantenida con Claude Code

- Estado: Aceptado
- Fecha: 2026-11-13

## Contexto

Nadie lee la documentación completa: se consulta para resolver algo concreto, a menudo a través de una IA.
Sin reglas, la documentación generada crece hasta que deja de leerse y se desactualiza sin que nadie lo note.

## Decisión

- La documentación se consulta, escribe y valida con tres skills: `/docs-ask`, `/docs-write` y `/docs-check`.
- Las reglas de escritura viven en un solo archivo, `.claude/rules/docs.md`, que Claude carga al tocar `docs/`.
- `docs/README.md` es el índice. Claude lo lee primero y abre solo el documento necesario.
- Lo mecánico (enlaces, rutas, tareas de `make`, índices y formato) lo valida `make docs-lint` sin IA. Que el texto diga la verdad lo valida `/docs-check`.
- `.claude/settings.json` bloquea a Claude las tareas que despliegan o borran datos.

## Consecuencias

- Cada documento tiene un máximo de 100 líneas y cada dato vive en un solo sitio.
- `/docs-ask` y `/docs-check` no pueden modificar archivos. `/docs-check` informa y las correcciones se piden después.
- Un documento que no esté en su índice hace fallar el lint.
- Los bloqueos de `.claude/settings.json` cubren la forma habitual de cada comando. No son un sandbox.

## Dónde verlo

`.claude/skills/`, `.claude/rules/docs.md`, `.claude/settings.json`, `CLAUDE.md` y `scripts/docs-lint.sh`.
