# 0007 — Pipeline en cada push y releases por tag

- Estado: Aceptado
- Fecha: 2026-11-12

## Contexto

Se quiere validar cada cambio y publicar versiones con el menor número de pasos posible.

## Decisión

- Se trabaja directo sobre `main`, con mensajes en formato Conventional Commits.
- Todo push a cualquier rama ejecuta el workflow **Pipeline**.
- Una versión se publica con un tag `v*`, que es la única fuente de la versión.
- El workflow **Release** espera al Pipeline de ese commit, construye la imagen con `docker/Dockerfile.prod`, la publica en `ghcr.io` y crea el GitHub Release con notas de git-cliff.

## Consecuencias

- Publicar es un solo comando, `make release`. Los pasos están en [RELEASE.md](../RELEASE.md).
- El tag no vuelve a ejecutar los tests: reutiliza el resultado del Pipeline del commit.
- No se publica ninguna imagen si el Pipeline del commit falló.
- Las notas del Release dependen de Conventional Commits, que no se valida.
- Cada job de los workflows pide solo los permisos que necesita.

## Dónde verlo

`.github/workflows/pipeline.yml`, `.github/workflows/release.yml` y `scripts/release.sh`.
