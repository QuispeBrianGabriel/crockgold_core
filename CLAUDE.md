# Crockgold Core

API Spring Boot 4 (Java 25) con PostgreSQL 18. Se trabaja dentro del Dev Container, directo sobre `main` y con Conventional Commits.

- Tareas: `make help`. `make` siempre sale con código 0, así que para validar se usa `./gradlew check`.
- Documentación: el índice es `docs/README.md`. Ante una duda del proyecto, leer el índice y solo el documento que corresponda.
- Skills de documentación: `/docs-ask` (consultar), `/docs-write` (escribir) y `/docs-check` (validar).
- El Dev Container no tiene `docker`. Lo que lo necesite se le indica al usuario para ejecutarlo en el host.

## Límites

- Un tag `v*` despliega a producción sin aprobación. No crear tags ni ejecutar `make release`.
- No ejecutar `make db-restore`, `make db-reset` ni comandos con `DB=` que apunten fuera de la base local.
- No leer ni mostrar `docker/.env`.
