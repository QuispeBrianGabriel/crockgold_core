# 0008 — Despliegue en producción con Railway

- Estado: Aceptado
- Fecha: 2026-11-12

## Contexto

Se requiere desplegar en producción con el menor costo y la menor complejidad posibles.
Plataformas como Kubernetes resuelven más casos, pero exigen más infraestructura, configuración y mantenimiento.

## Decisión

- Producción se despliega en **Railway**, configurado fuera del repositorio.
- Railway apunta a la imagen `ghcr.io/...:latest`.
- Cada release publica una imagen nueva y mueve `:latest`; Railway detecta el cambio y despliega.
- No hay ambiente de testing ni aprobación manual.

## Consecuencias

- Publicar un tag `v*` termina en producción sin pasos extra.
- La única barrera antes de producción es el Pipeline del commit.
- Nada del despliegue vive en el código: ni Railway ni sus ajustes aparecen en el repositorio.
- La imagen no depende de Railway y debe seguir así, para poder migrar a Kubernetes u otra plataforma si se quisiera.

## Dónde verlo

- Fuera del repositorio, en la configuración del servicio en Railway.
- `.github/workflows/release.yml`: `docker/metadata-action` añade `:latest` por defecto a las versiones estables.
