# language: es
Característica: Documentación de la API
  Como desarrollador que integra un cliente con Crockgold
  Quiero consultar la documentación de la API
  Para saber qué operaciones ofrece y cómo usarlas

  Regla: La documentación describe cada operación que ofrece la API

    @happy
    Escenario: Un cliente con la credencial ve documentadas todas las operaciones
      Cuando un cliente con la credencial consulta la documentación de la API
      Entonces ve documentado el registro de usuarios
      Y ve documentado el inicio de sesión
      Y ve documentado el estado de salud

  Regla: La documentación indica qué operaciones se usan sin credencial

    @happy
    Escenario: Un cliente con la credencial ve qué operaciones no la piden
      Cuando un cliente con la credencial consulta la documentación de la API
      Entonces ve que el registro de usuarios no pide credencial
      Y ve que el inicio de sesión no pide credencial
      Y ve que el estado de salud no pide credencial
      Y ve que las demás operaciones piden la credencial de la API

  Regla: La documentación se puede explorar desde el navegador

    @happy
    Escenario: Un cliente con la credencial explora la documentación desde el navegador
      Cuando un cliente con la credencial abre la documentación en el navegador
      Entonces ve la documentación de la API
