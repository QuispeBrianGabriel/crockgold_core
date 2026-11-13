#!/usr/bin/env bash
# -----------------------------------------------------------------------------
# Prueba rápida de que la API responde como se espera:
#   1. /actuator/health        → 200
#   2. /  sin credenciales     → 401 (la seguridad está activa)
#   3. /  con credenciales     → 404 (credenciales aceptadas; aún no hay endpoints)
#
# Uso: scripts/smoke.sh [URL] [USUARIO] [CLAVE]
#   Por defecto apunta a la API local con las credenciales del Dev Container.
#   Para producción: scripts/smoke.sh https://... usuario clave
# -----------------------------------------------------------------------------
source "$(dirname "$0")/_common.sh"

URL="${1:-http://localhost:8080}"
USUARIO="${2:-${SPRING_SECURITY_USER_NAME:-user}}"
CLAVE="${3:-${SPRING_SECURITY_USER_PASSWORD:-}}"

failures=0

# Compara el código HTTP obtenido con el esperado e imprime el resultado.
# Si la API no responde, curl devuelve "000" y la comprobación falla.
expect() {
	local name="$1" expected="$2"
	shift 2
	local got
	got=$(curl -s -o /dev/null -w '%{http_code}' "$@" || true)

	if [[ "$got" == "$expected" ]]; then
		printf '%s✔%s %-20s %s\n' "$GREEN" "$RESET" "$name" "$got"
	else
		printf '%s✘%s %-20s %s (esperado %s)\n' "$RED" "$RESET" "$name" "$got" "$expected"
		failures=$((failures + 1))
	fi
}

info "Probando $URL"
expect "health" 200 "$URL/actuator/health"
expect "sin credenciales" 401 "$URL/"
expect "con credenciales" 404 -u "$USUARIO:$CLAVE" "$URL/"

if [[ $failures -gt 0 ]]; then
	fail "$failures comprobación(es) fallaron"
fi
ok "Todo responde como se espera"
