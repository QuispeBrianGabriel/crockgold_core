# 0007 — Pipeline en cada push y releases por tag

- Estado: Aceptado
- Fecha: 2026-11-12

## Contexto

Se quiere validar cada cambio y publicar versiones con el menor número de pasos posible.

## Decisión

**Trabajo diario**

- Se trabaja directo sobre `main`, con mensajes en formato *Conventional Commits*.
- Todo push a cualquier rama ejecuta el workflow **Pipeline**.

**Publicar una versión**

- Una versión se publica con un tag `v*` (ej `v0.3.0`): El tag es la única fuente de la versión.
- El workflow **Release** hace lo siguiente en este orden:
  1. Espera al Pipeline de ese mismo commit y se detiene si falló.
  2. Construye la imagen con `docker/Dockerfile.prod`.
  3. La publica en GitHub Container Registry (`ghcr.io`), etiquetada con la versión.
  4. Crea el GitHub Release, con notas  gitgeneradas por-cliff a partir de los commits.

## Consecuencias

- Publicar es un solo comando: `git tag v0.3.0 && git push origin main v0.3.0`.
- El tag no vuelve a ejecutar los tests: se reutiliza el resultado del Pipeline del commit.
- No se publica ninguna imagen si el Pipeline del commit falló.
- Las notas del release requieren Conventional Commits, el cual **no se valida**.
- La versión no está escrita en ningún archivo del proyecto.
- Cada job de los workflows pide solo los permisos que necesita.

## Dónde verlo

- `.github/workflows/pipeline.yml`
- `.github/workflows/release.yml`
- `README.md` (sección «Integración continua y releases»)
