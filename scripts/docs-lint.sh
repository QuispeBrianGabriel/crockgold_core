#!/usr/bin/env bash
# -----------------------------------------------------------------------------
# Valida la documentación sin IA: todo lo que se puede comprobar sin entender
# el texto. Revisa README.md y docs/**/*.md.
#
#   1. Enlaces relativos: el archivo existe y el ancla (#sección) también.
#   2. Rutas del repo entre `comillas`: existen.
#   3. Tareas `make <tarea>`: están en el Makefile.
#   4. Índices: cada documento está enlazado desde el README.md de su carpeta.
#   5. Formato: máximo de líneas, sin `;` en la prosa y nodos de Mermaid cortos.
#
# Uso: scripts/docs-lint.sh
#
# Las reglas están en .claude/rules/docs.md. Que el texto diga la verdad no se
# puede comprobar aquí: eso lo revisa Claude con /docs-check.
# -----------------------------------------------------------------------------
source "$(dirname "$0")/_common.sh"
cd "$(dirname "$0")/.."

# Con UTF-8, sed y grep tratan «á» o «ñ» como letras al calcular las anclas.
export LC_ALL=C.UTF-8

MAX_LINES=100
MAX_WORDS=6
# Rutas documentadas que todavía no existen (se crean con la primera migración).
PENDING=" src/main/resources/db/migration/ "

mapfile -t DOCS < <(find README.md docs -name '*.md' | sort)
TASKS=" $(grep -oE '^[a-z][a-z-]*:' Makefile | tr -d ':' | tr '\n' ' ')"

problems=0
report() {
	printf '%s:%s: %s\n' "$1" "$2" "$3"
	problems=$((problems + 1))
}

# Líneas fuera de los bloques de código, como "<n.º de línea><TAB><texto>".
prose() {
	awk '/^ *```/ { fence = !fence; next } !fence { print NR "\t" $0 }' "$1"
}

# Anclas que GitHub genera para los títulos de un archivo.
anchors() {
	awk '/^ *```/ { fence = !fence } !fence && /^#+ / { sub(/^#+ +/, ""); print }' "$1" |
		sed -E 's/`//g; s/.*/\L&/; s/[^[:alnum:] _-]//g; s/ /-/g'
}

for doc in "${DOCS[@]}"; do
	dir=$(dirname "$doc")

	while IFS=$'\t' read -r n line; do
		# --- 1. Enlaces relativos ---------------------------------------------
		while read -r link; do
			link=${link#*](}
			link=${link%)}
			[[ "$link" =~ ^(https?:|mailto:) ]] && continue
			file=${link%%#*}
			anchor=""
			[[ "$link" == *"#"* ]] && anchor=${link#*#}
			target=$doc
			[[ -n "$file" ]] && target="$dir/$file"
			if [[ ! -e "$target" ]]; then
				report "$doc" "$n" "enlace roto: $link"
			elif [[ -n "$anchor" ]] && ! anchors "$target" | grep -qxF -- "$anchor"; then
				report "$doc" "$n" "no existe la sección #$anchor en $target"
			fi
		done < <([[ "$line" == *"]("* ]] && grep -oE '\]\([^)]+\)' <<<"$line" || true)

		# --- 2. Rutas del repo y 3. tareas de make (texto entre `comillas`) ----
		while read -r code; do
			code=${code//\`/}
			if [[ "$code" =~ ^make\ ([a-z][a-z-]*) ]]; then
				task=${BASH_REMATCH[1]}
				[[ "$TASKS" == *" $task "* ]] || report "$doc" "$n" "la tarea no existe: make $task"
			elif [[ "$code" =~ ^[A-Za-z0-9_.-]+/[A-Za-z0-9_./-]*$ && -e "${code%%/*}" ]]; then
				[[ -e "$code" || "$PENDING" == *" $code "* ]] && continue
				git check-ignore -q "$code" && continue
				report "$doc" "$n" "la ruta no existe: $code"
			fi
		done < <([[ "$line" == *\`* ]] && grep -oE '`[^`]+`' <<<"$line" || true)

		# --- 5. Formato: sin `;` en la prosa ------------------------------------
		if [[ "$line" == *";"* && "$(sed -E 's/`[^`]*`//g' <<<"$line")" == *";"* ]]; then
			report "$doc" "$n" "hay un ';' en la prosa: usa dos frases o una lista"
		fi
	done < <(prose "$doc")

	# --- 3. Tareas de make dentro de bloques de código -------------------------
	while IFS=$'\t' read -r n task; do
		[[ "$TASKS" == *" $task "* ]] || report "$doc" "$n" "la tarea no existe: make $task"
	done < <(awk '/^ *```/ { fence = !fence; next }
		fence && $1 == "make" && $2 ~ /^[a-z][a-z-]*$/ { print NR "\t" $2 }' "$doc")

	# --- 5. Formato: nodos de Mermaid cortos -----------------------------------
	while IFS=$'\t' read -r n line; do
		while read -r label; do
			words=$(wc -w <<<"$label")
			[[ $words -le $MAX_WORDS ]] || report "$doc" "$n" "nodo de Mermaid con $words palabras (máximo $MAX_WORDS)"
		done < <(grep -oE '"[^"]+"|\[[^]["]+\]|\|[^|]+\||: .+$' <<<"$line" || true)
	done < <(awk '/^ *```mermaid/ { mermaid = 1; next } /^ *```/ { mermaid = 0 } mermaid { print NR "\t" $0 }' "$doc")

	# --- 5. Formato: máximo de líneas -------------------------------------------
	lines=$(wc -l <"$doc")
	[[ $lines -le $MAX_LINES ]] || report "$doc" 1 "tiene $lines líneas (máximo $MAX_LINES): recorta o divide"

	# --- 4. Índices -------------------------------------------------------------
	index="$dir/README.md"
	if [[ "$doc" == docs/* && "$doc" != "$index" ]] && ! grep -qsF "($(basename "$doc")" "$index"; then
		report "$doc" 1 "no está enlazado desde $index"
	fi
done

if [[ $problems -eq 0 ]]; then
	ok "Documentación correcta: ${#DOCS[@]} archivos sin problemas mecánicos"
else
	printf '%s✘%s %d problemas en la documentación\n' "$RED$BOLD" "$RESET" "$problems"
fi
