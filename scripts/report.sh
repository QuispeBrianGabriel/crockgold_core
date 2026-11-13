#!/usr/bin/env bash
# -----------------------------------------------------------------------------
# Abre en el navegador del HOST los dos informes que deja `make test`:
#   - Resultados de los tests (Gradle):  build/reports/tests/test/
#   - Cobertura de código (JaCoCo):      build/reports/jacoco/test/html/
#
# Los archivos viven dentro del contenedor, así que el navegador no puede
# abrirlos como file://. Por eso se sirve build/reports/ por HTTP en el 8090:
#   - jwebserver: servidor estático que ya viene con el JDK (nada que instalar).
#   - VS Code reenvía el puerto al host (forwardPorts en devcontainer.json).
#   - $BROWSER: variable que define VS Code para abrir una URL en el host.
#
# Si solo existe uno de los dos informes, abre ese y avisa del otro.
# El servidor queda en primer plano: Ctrl+C para cerrarlo.
# -----------------------------------------------------------------------------
source "$(dirname "$0")/_common.sh"

ROOT="$PWD/build/reports"
PORT=8090
BASE="http://localhost:$PORT"
INTERRUPT_MSG="👋 Servidores cerrados correctamente"

# Nombre del informe → ruta dentro de build/reports/
declare -A REPORTS=(
	["Tests"]="tests/test/index.html"
	["Cobertura"]="jacoco/test/html/index.html"
)

# Solo se abren los informes que existen.
urls=()
for name in Tests Cobertura; do
	if [[ -f "$ROOT/${REPORTS[$name]}" ]]; then
		urls+=("$name|$BASE/${REPORTS[$name]}")
	else
		printf '%s⚠ Falta informe de %s: generarlo con make test%s\n' "$YELLOW" "$name" "$RESET"
	fi
done

if [[ ${#urls[@]} -eq 0 ]]; then
	fail "Aún no hay informes: ejecuta antes make test"
fi

# Un servidor que ya ocupa el puerto (otro make report abierto) haría fallar
# a jwebserver con un error poco claro: se avisa antes.
if curl -s -o /dev/null "$BASE/"; then
	fail "El puerto $PORT está en uso: ¿hay otro make report abierto?"
fi

# Arranca el servidor en segundo plano para poder abrir el navegador.
# at_exit lo cierra al salir del script, también con Ctrl+C.
jwebserver --directory "$ROOT" --port "$PORT" --output none >/dev/null &
server=$!
stop_server() {
	kill "$server" 2>/dev/null || true
}
at_exit stop_server

# Espera a que el puerto responda (máx. ~5 s) antes de abrir el navegador.
for _ in {1..50}; do
	curl -s -o /dev/null "$BASE/" && break
	sleep 0.1
done
if ! curl -s -o /dev/null "$BASE/"; then
	fail "El servidor no arrancó en el puerto $PORT"
fi

for entry in "${urls[@]}"; do
	name="${entry%%|*}"
	url="${entry#*|}"
	if [[ -n "${BROWSER:-}" ]]; then
		"$BROWSER" "$url" >/dev/null 2>&1 || true
		ok "Informe de $name abierto en: $url"
	else
		# Fuera de VS Code no hay $BROWSER: basta con abrir la URL a mano.
		info "Informe de $name: abre en $url"
	fi
done

printf '%sCtrl+C para cerrar los servidores%s\n' "$DIM" "$RESET"
wait "$server" || fail "El servidor de informes se cerró de forma inesperada"
