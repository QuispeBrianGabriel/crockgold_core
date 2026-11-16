# language: es
Característica: Inicio de sesión
  Como usuario registrado
  Quiero iniciar sesión
  Para ingresar al sistema

  Antecedentes:
    Dado que los siguientes usuarios están registrados:
      | email            | contraseña            |
      | john@example.com | correct-horse-battery |
      | jane@example.com | ␣Pass␣word␣1␣         |
      | lucy@example.com | [72×a]                |

  Regla: El email y la contraseña deben coincidir con un registro existente

    @happy
    Escenario: Inicia sesión con su email y su contraseña
      Cuando alguien inicia sesión con cada uno de estos datos:
        | email              | contraseña            |
        | john@example.com   | correct-horse-battery |
        | JOHN@Example.com   | correct-horse-battery |
        | ␣john@example.com␣ | correct-horse-battery |
        | jane@example.com   | ␣Pass␣word␣1␣         |
      Entonces se le reconoce como cada uno de estos usuarios:
        | email            |
        | john@example.com |
        | john@example.com |
        | john@example.com |
        | jane@example.com |

    @happy @border
    Escenario: Inicia sesión con su contraseña de la longitud máxima
      Cuando alguien inicia sesión con cada uno de estos datos:
        | email            | contraseña |
        | lucy@example.com | [72×a]     |
      Entonces se le reconoce como cada uno de estos usuarios:
        | email            |
        | lucy@example.com |

  Regla: Un intento fallido no revela si falló el email o la contraseña

    @unhappy
    Escenario: No inicia sesión con un email o una contraseña que no coinciden
      Cuando alguien inicia sesión con cada uno de estos datos:
        | email              | contraseña            |
        | john@example.com   | wrong-password        |
        | john@example.com   | Correct-Horse-Battery |
        | john@example.com   | córrect-horse-battery |
        | jane@example.com   | Pass␣word␣1           |
        | john@example.com   | ␣Pass␣word␣1␣         |
        | nobody@example.com | correct-horse-battery |
      Entonces se rechaza porque las credenciales son incorrectas

    @unhappy @border
    Escenario: No inicia sesión con su contraseña de la longitud máxima y un carácter de más o de menos
      Cuando alguien inicia sesión con cada uno de estos datos:
        | email            | contraseña |
        | lucy@example.com | [73×a]     |
        | lucy@example.com | [71×a]     |
      Entonces se rechaza porque las credenciales son incorrectas

  Regla: El email y la contraseña son obligatorios, y el email debe ser una dirección válida

    @unhappy
    Escenario: No inicia sesión sin email o sin contraseña
      Cuando alguien inicia sesión con cada uno de estos datos:
        | email            | contraseña            |
        | [nulo]           | correct-horse-battery |
        | [vacío]          | correct-horse-battery |
        | [3×␣]            | correct-horse-battery |
        | john@example.com | [nulo]                |
        | john@example.com | [vacío]               |
        | john@example.com | [8×␣]                 |
      Entonces se rechaza porque faltan datos

    @unhappy
    Escenario: No inicia sesión con un email sin exactamente una arroba
      Cuando alguien inicia sesión con cada uno de estos datos:
        | email | contraseña            |
        | plain | correct-horse-battery |
      Entonces se rechaza porque el email no tiene exactamente una arroba

    @unhappy
    Escenario: No inicia sesión con un email cuya parte anterior a la arroba no es válida
      Cuando alguien inicia sesión con cada uno de estos datos:
        | email             | contraseña            |
        | .john@example.com | correct-horse-battery |
      Entonces se rechaza porque la parte anterior a la arroba no es válida

    @unhappy
    Escenario: No inicia sesión con un email cuyo dominio no es válido
      Cuando alguien inicia sesión con cada uno de estos datos:
        | email          | contraseña            |
        | john@localhost | correct-horse-battery |
      Entonces se rechaza porque el dominio del email no es válido

    @unhappy @border
    Escenario: No inicia sesión con un email demasiado largo
      Cuando alguien inicia sesión con cada uno de estos datos:
        | email                             | contraseña            |
        | a@[63×b].[63×b].[63×b].[57×c].com | correct-horse-battery |
      Entonces se rechaza porque el email es demasiado largo

    @unhappy @border
    Escenario: No inicia sesión con un email cuya parte anterior a la arroba es demasiado larga
      Cuando alguien inicia sesión con cada uno de estos datos:
        | email              | contraseña            |
        | [65×a]@example.com | correct-horse-battery |
      Entonces se rechaza porque la parte anterior a la arroba es demasiado larga
