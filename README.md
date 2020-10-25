# spring-boot-casino
# Evaluación: Casino
# por Jordi Marsal

*Sabadell / Octubre 2020*

Implementación conceptual de backend aplicación de un casino simplificado donde los
jugadores pueden realizar apuestas y obtener ganancias.
- Aplicación desarrollada con __Java OpenJDK 11__.


## Contenido

- [Inicio](#inicio)
- [Comentarios](#comentarios)
- [Funcionamiento Automático](#funcionamiento)
- [Funcionamiento Manual](#funcionamiento)
- [Links](#links)


## Inicio

__Opcion 1:__
- Clonar el repositorio.
- Importar el proyecto como __Maven__.
- Con el plugin para __Eclipse__: __Spring Tools 4__ ya instalado.
- Ejecutar en el __*Boot Dashboard*__

__Opcion2:__
- Descargar el archivo ejecutable '__spring-boot-casino-j11.jar__'
- Abrir una consola.
- Introducir en la línea de comandos: __java -jar spring-boot-casino-j11.jar__
- NOTA IMPORTANTE: En algunas ejecuciones desde __java -jar__ el jobrunner se queda aparentemente detenido, afectando incluso el acceso desde __localhost__. He comprobado que en ese caso apretar la combinación de teclas __Crtl + c__, pulsada una sola vez, permite que el flujo de trabajos siga funcionando. (Más de una vez detendrá la ejecución del jar).


## Comentarios

- No se usa base de datos, aunque algunas entidades se han colocado en los packages __entity__ y __dao__ por su función.
- En el log se muestran salidas DEBUG e INFO, se puede cambiar la configuración en *__logback-spring.xml__*


## Funcionamiento Automático

- Automatización con __JobRunr__: 
- Cada minuto, el programa ingresa un *Player* nuevo con un tiempo disponible el cual efectúa una serie de apuestas.
- Cada vez que ingresa un nuevo *Player* se purga cualquier *Player* que estuviera con tiempo caducado (para evitar que crezca el consumo de memoria).
- Se puede observar el devenir de los trabajos de __JobRunr__ en http://localhost:8000/, el cual se instala por defecto.
- Cada transacción se muestra en el log.


## Funcionamiento Manual

- Una vez ejecutado un servicio REST estarà disponible en __http://localhost:9095/api/casino/__
- Se adjunta una colección de *Requests* recopiladas en [Postman Importar](https://learning.postman.com/docs/getting-started/importing-and-exporting-data/)
- los endpoints disponibles son:

| Endpoint | Return |
| :--- | :--- |
| login | String |
| logout | String |
| bet | String |
| get | Player |
| gets | String |

```
Como prueba a modo de verificación de la url: http://localhost:9095/api/casino/gets/TEST-UUID-01
```

## Links 

* [Este repositorio](https://github.com/jordimarsal/spring-boot-casino)
* [Postman](https://learning.postman.com/)
* [JobRunr](https://www.jobrunr.io/en/)
