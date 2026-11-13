# 0008 — Despliegue en producción con Railway

- Estado: Aceptado
- Fecha: 2026-11-12

## Contexto

Se quiere desplegar con el menor costo y la menor complejidad posibles.
Plataformas como Kubernetes resuelven más casos, pero exigen más infraestructura y mantenimiento.

## Decisión

- Producción se despliega en **Railway**, configurado fuera del repositorio.
- Railway apunta a la imagen `ghcr.io/...:latest`.
- Cada release estable mueve `:latest`. Railway detecta el cambio y despliega.
- No hay ambiente de pruebas ni aprobación manual.

## Consecuencias

- Publicar un tag `v*` estable termina en producción sin pasos extra.
- La única barrera antes de producción es el Pipeline del commit.
- Nada del despliegue vive en el código.
- La imagen no depende de Railway y debe seguir así, para poder migrar a otra plataforma.

## Dónde verlo

En la configuración del servicio en Railway y en `.github/workflows/release.yml`, donde `metadata-action` añade `:latest` a las versiones estables.
