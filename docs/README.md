# Documentación

| Documento | Responde a |
| --------- | ---------- |
| [DEVELOPMENT.md](DEVELOPMENT.md) | Montar el entorno, tareas diarias, base local, migraciones y fallos habituales |
| [CONFIGURATION.md](CONFIGURATION.md) | Variables de entorno, credenciales y cómo rotarlas |
| [RELEASE.md](RELEASE.md) | Publicar una versión y fallos del workflow Release |
| [RUNBOOK.md](RUNBOOK.md) | Producción falla: diagnóstico, reversión y restauración |
| [RISKS.md](RISKS.md) | Huecos operativos conocidos |
| [adr/](adr/README.md) | Por qué se tomó cada decisión |

## Con Claude Code

Nadie necesita leerlo todo. Dentro de Claude Code:

| Necesidad | Comando |
| --------- | ------- |
| Preguntar | `/docs-ask ¿cómo roto la contraseña de la API?` |
| Escribir o actualizar | `/docs-write documenta la tarea db-dump` |
| Validar contra el código | `/docs-check` o `/docs-check docs/RELEASE.md` |

- `/docs-ask` responde desde `docs/` y cita la sección de origen. No puede ejecutar ni modificar nada.
- `/docs-write` aplica las reglas de `.claude/rules/docs.md` y solo toca `docs/` y `README.md`.
- `/docs-check` informa y no modifica nada. Las correcciones se piden después.
- `make docs-lint` valida sin IA los enlaces, las rutas, las tareas de `make` y el formato.

El porqué está en el [ADR 0010](adr/0010-documentacion-con-claude-code.md).
