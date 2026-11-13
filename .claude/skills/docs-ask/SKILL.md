---
name: docs-ask
description: Responde preguntas sobre este proyecto (entorno, configuración, releases, incidencias, decisiones) usando docs/ como fuente y citando la sección de origen. Usar cuando alguien pregunte cómo se hace algo o por qué algo es así en este repositorio.
argument-hint: <pregunta>
disallowed-tools: Bash Edit Write NotebookEdit
effort: low
---

Pregunta: $ARGUMENTS

1. Lee `docs/README.md` (el índice) y elige el documento que responde. Si no queda claro, busca las palabras clave con Grep en `docs/`.
2. Lee solo ese documento, o solo la sección si ya la localizaste. No leas toda la carpeta.
3. Responde en 10 líneas como máximo, con los pasos o el dato concreto.
4. Termina con **Fuente:** y el enlace a la sección, por ejemplo `[RUNBOOK › Revertir](docs/RUNBOOK.md#2-revertir-a-la-versión-anterior)`.

Reglas:

- La fuente es `docs/`. No completes con conocimiento general ni con suposiciones.
- Si la documentación no lo cubre, dilo. Puedes buscar la respuesta en el código y darla marcada como **No documentado**, con el archivo de origen, y sugerir `/docs-write`.
- Si la documentación contradice al código, avisa y sugiere `/docs-check`.
- No ejecutes comandos ni modifiques archivos.
