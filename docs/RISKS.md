# Riesgos operativos conocidos

Huecos a fecha 2026-11-13. Cada uno debería cerrarse o aceptarse de forma explícita.

| Hueco | Consecuencia |
| ----- | ------------ |
| Sin copias de seguridad definidas ni restauración ensayada | Una pérdida de la base puede ser definitiva |
| Sin monitoreo ni alertas | Una caída se descubre cuando alguien la reporta |
| Reversión sin ensayar | El primer intento será durante una incidencia real |
| Sin ambiente de pruebas ni aprobación manual | La única barrera antes de producción es el Pipeline |
| El Pipeline no construye la imagen de producción | Un `Dockerfile.prod` roto se descubre al publicar el tag |
| Configuración de Railway sin versionar | Producción solo se puede reconstruir a mano desde [CONFIGURATION.md](CONFIGURATION.md) |
| Una única credencial HTTP Basic compartida | No se distingue a los clientes y rotarla los afecta a todos |
| Conventional Commits sin validar | Las notas de un Release pueden salir incompletas |
| URL de producción y responsable sin documentar | Están **Por confirmar** en [RUNBOOK.md](RUNBOOK.md) |
