#!/usr/bin/env bash
# -----------------------------------------------------------------------------
# Publica una versión: crea el tag vX.Y.Z y lo sube junto con `main`.
# Un tag estable DESPLIEGA a producción (ver docs/RELEASE.md).
#
# Uso: scripts/release.sh <X.Y.Z>
#   Ej:  scripts/release.sh 0.3.4
#        scripts/release.sh 0.4.0-rc.1   (prelanzamiento: no despliega)
#
# Antes de crear nada comprueba que:
#   - la versión tiene formato SemVer,
#   - estás en `main` y sin cambios pendientes,
#   - el tag no existe ya.
#
# Es el ÚNICO lugar que crea tags: si al final no se publica (se cancela la
# confirmación o falla el push), no queda ningún tag creado.
# -----------------------------------------------------------------------------
source "$(dirname "$0")/_common.sh"

VERSION="${1:-}"
TAG="v$VERSION"
LAST=$(git describe --tags --abbrev=0 2>/dev/null || echo "ninguna")

# X.Y.Z con un sufijo opcional de prelanzamiento, como -rc.1
if [[ ! "$VERSION" =~ ^[0-9]+\.[0-9]+\.[0-9]+(-[0-9A-Za-z.]+)?$ ]]; then
	fail "La versión debe ser SemVer. Ej: make release V=0.3.4 (última: $LAST)"
fi
if [[ "$(git branch --show-current)" != main ]]; then
	fail "Hay que estar en main"
fi
if [[ -n "$(git status --porcelain)" ]]; then
	fail "Hay cambios sin commitear"
fi
if git rev-parse --quiet --verify "refs/tags/$TAG" >/dev/null; then
	fail "El tag $TAG ya existe"
fi

info "Última versión: $LAST  →  nueva: $TAG"
confirm "Si es estable, $TAG se desplegará a producción. ¿Seguir?"

# Este script es el único que crea tags, y no debe dejar ninguno sin publicar.
# Si el push falla o se interrumpe (Ctrl+C), la limpieza borra el tag local.
published=false
cleanup() {
	if ! $published; then
		git tag -d "$TAG" >/dev/null 2>&1 || true
		printf '%s✘%s %s\n' "$RED$BOLD" "$RESET" "No se publicó: el tag $TAG se ha borrado" >&2
	fi
}

git tag "$TAG"
at_exit cleanup

# --atomic: o se suben `main` y el tag juntos, o no se sube nada.
if ! git push --atomic origin main "$TAG"; then
	fail "git push falló (revisa la salida de arriba)"
fi
published=true

ok "$TAG publicada. Sigue el workflow Release en GitHub"
