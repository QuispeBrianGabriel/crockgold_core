#!/usr/bin/env bash
# -----------------------------------------------------------------------------
# Guarda una copia de seguridad de una base en dumps/<fecha>.dump.
# Se restaura con scripts/db-restore.sh.
#
# Uso: scripts/db-dump.sh [URL]
#   Sin URL → la base local del Dev Container.
#   Con URL → cualquier otra, ej. producción:
#             scripts/db-dump.sh 'postgresql://usuario:clave@host:5432/crockgold'
#
# Las copias van a dumps/ (ignorada por git) y no a build/, para que
# `make clean` no las borre.
# -----------------------------------------------------------------------------
source "$(dirname "$0")/_common.sh"

DB="${1:-}"
if [[ -z "$DB" ]]; then
	use_local_db
fi

DIR=dumps
mkdir -p "$DIR"

file="$DIR/$(date +%Y%m%d-%H%M%S).dump"
info "Copiando $(redact "$DB")"

# Si pg_dump falla o se interrumpe, deja un archivo a medias: se borra para
# que nadie lo confunda con una copia válida.
done=false
remove_partial() {
	if ! $done; then
		rm -f "$file"
	fi
}
at_exit remove_partial

if ! pg_dump --dbname="$DB" --format=custom --file="$file"; then
	fail "No se pudo hacer la copia de $(redact "$DB") (revisa el error de arriba)"
fi
done=true
ok "Copia guardada en $file"
