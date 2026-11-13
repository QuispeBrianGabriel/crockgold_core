#!/usr/bin/env bash
# -----------------------------------------------------------------------------
# Restaura una copia hecha con scripts/db-dump.sh.
# REEMPLAZA los datos actuales, por eso pide confirmación.
#
# Uso: scripts/db-restore.sh <archivo.dump> [URL]
#   Sin URL → la base local del Dev Container.
#   Con URL → cualquier otra, ej. producción (haz antes un db-dump de ella).
# -----------------------------------------------------------------------------
source "$(dirname "$0")/_common.sh"

FILE="${1:-}"
DB="${2:-}"

if [[ ! -f "$FILE" ]]; then
	info "Copias disponibles en dumps/:"
	ls -1 dumps 2>/dev/null || echo "  (ninguna)"
	fail "Indica una copia existente. Ej: make db-restore FILE=dumps/20261113-120000.dump"
fi

if [[ -z "$DB" ]]; then
	use_local_db
fi

confirm "Se reemplazarán los datos de $(redact "$DB") con $FILE. ¿Seguir?"

# --clean borra cada objeto antes de recrearlo; --if-exists evita errores
# cuando el objeto no existe todavía.
if ! pg_restore --dbname="$DB" --clean --if-exists "$FILE"; then
	fail "La restauración terminó con errores (revisa los mensajes de arriba)"
fi
ok "Base restaurada"
