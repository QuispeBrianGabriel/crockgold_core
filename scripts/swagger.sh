#!/usr/bin/env bash
# -----------------------------------------------------------------------------
# Abre Swagger UI de la API en el navegador del HOST.
#
# Uso: scripts/swagger.sh [URL]
#   Por defecto, la API local de `make run` (http://localhost:8080).
#
# - La API debe estar arrancada: este script no la levanta.
# - Swagger UI pide la credencial de la API (HTTP Basic, ADR 0006): el
#   navegador la pregunta al abrirlo. En local es la del Dev Container.
# - $BROWSER: variable que define VS Code para abrir una URL en el host.
# -----------------------------------------------------------------------------
source "$(dirname "$0")/_common.sh"

URL="${1:-http://localhost:8080}"
UI="$URL/swagger-ui/index.html"

if ! curl -s -o /dev/null "$URL/actuator/health"; then
	fail "La API no responde en $URL: arráncala antes con make run"
fi

if [[ -n "${BROWSER:-}" ]]; then
	"$BROWSER" "$UI" >/dev/null 2>&1 || true
	ok "Swagger UI abierto en: $UI"
else
	# Fuera de VS Code no hay $BROWSER: basta con abrir la URL a mano.
	info "Swagger UI: abre en $UI"
fi
printf '%sCredencial: usuario %s y la contraseña de SPRING_SECURITY_USER_PASSWORD%s\n' \
	"$DIM" "${SPRING_SECURITY_USER_NAME:-user}" "$RESET"
