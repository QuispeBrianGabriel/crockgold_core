---
paths:
  - "docs/**/*.md"
  - "README.md"
---

# Reglas de documentación

Lector: un dev, con o sin experiencia, o una IA que busca resolver algo concreto. Nadie lee todo, así que cada sección debe servir sola.

## Contenido

- Cada dato vive en un solo documento. En los demás se enlaza, no se repite.
- Solo lo que no es obvio leyendo el código o `make help`. Sin introducciones, sin historia y sin explicar herramientas conocidas.
- Solo hechos verificados contra el repo. Lo que no se puede verificar se marca **Por confirmar**.
- Antes de crear un documento se amplía uno existente. Uno nuevo se añade al índice `docs/README.md`.
- Al añadir se recorta: lo que el cambio deja obsoleto o duplicado se borra en el mismo cambio.

## Forma

- Listas y tablas antes que prosa. Párrafos de 4 renglones como máximo.
- Sin `;` en la prosa: van dos frases o una lista.
- Máximo 100 líneas por documento. Si no cabe, sobra contenido o son dos documentos.
- Comandos: la tarea de `make`, no lo que ejecuta debajo.
- Bloques de código de 8 líneas como máximo. Si un procedimiento necesita más, se automatiza en `scripts/` y se documenta la tarea.
- Mermaid solo para flujos o relaciones que el texto no deja claros. Máximo 6 palabras por nodo: el detalle va en texto fuera del diagrama, sin repetirlo.
- Enlaces relativos y con ancla a la sección: `[rotar credenciales](CONFIGURATION.md#rotar-credenciales)`.
- En español, con frases impersonales o en infinitivo ("Ejecutar", "Revisar").

## ADR

- Uno por decisión, en `docs/adr/NNNN-titulo.md`, con el número siguiente y su fila en `docs/adr/README.md`.
- Secciones fijas: Contexto, Decisión, Consecuencias y Dónde verlo (en una línea). Máximo 35 líneas.
- Un ADR aceptado no se reescribe cuando la decisión cambia: se crea otro que lo reemplaza y se actualiza el estado del anterior.

## Comprobación

`make docs-lint` valida enlaces, rutas, tareas de `make`, índices y formato.
