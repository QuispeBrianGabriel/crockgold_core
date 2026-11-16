# language: es
Característica: Acceso a la API

  Como responsable de Crockgold
  Quiero que la API solo atienda a los clientes que presentan su credencial
  Para que nadie ajeno pueda usarla

  Regla: Sin credencial solo se puede comprobar que la aplicación está disponible

    Antecedentes:
      Dado un cliente sin credencial

    @happy
    Escenario: Un cliente sin credencial comprueba que la aplicación está disponible
      Cuando consulta el estado de salud
      Entonces ve que la aplicación está disponible

    @unhappy
    Escenario: Un cliente sin credencial no accede a la información de la aplicación
      Cuando consulta la información de la aplicación
      Entonces se le deniega el acceso

    @unhappy
    Escenario: Un cliente sin credencial no accede a una operación que no existe
      Cuando consulta una operación que no existe
      Entonces se le deniega el acceso

    @unhappy
    Escenario: Un cliente sin credencial no accede a la documentación de la API
      Cuando consulta la documentación de la API
      Entonces se le deniega el acceso

    @unhappy
    Escenario: Un cliente sin credencial no explora la documentación desde el navegador
      Cuando abre la documentación en el navegador
      Entonces se le deniega el acceso

  Regla: La credencial se presenta en cada consulta, porque la API no abre sesiones

    @happy
    Escenario: Un cliente con la credencial accede sin que se le abra una sesión
      Dado un cliente con la credencial de la API
      Cuando consulta la información de la aplicación
      Entonces se le permite el acceso
      Pero no se le abre ninguna sesión
