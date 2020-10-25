# spring-boot-casino
# Evaluación: Casino
# por Jordi Marsal

*Sabadell / Octubre 2020*

Implementación conceptual de backend aplicación de un casino simplificado donde los
jugadores pueden realizar apuestas y obtener ganancias.


## Contenido

- [Inicio](#inicio)
- [Comentarios](#comentarios)
- [Funcionamiento Automático](#funcionamiento)
- [Funcionamiento Manual](#funcionamiento)
- [Links](#links)


## Inicio

Opcion 1:
- Clonar el repositorio.
- Importar el proyecto como Maven.
- Con el plugin para Eclipse: Spring Tools 4 ya instalado.
- Ejecutar en el Boot Dashboard

Opcion2:
- Descargar el archivo ejecutable '__spring-boot-casino-j11.jar__'
- Abrir una consola.
- Introducir en la línea de comandos: __java -jar spring-boot-casino-j11.jar__


## Comentarios

- No se usa base de datos, aunque algunas entidades se han colocado en los packages __entity__ y __dao__ por su función.
- En el log se muestran salidas DEBUG e INFO, se puede cambiar la configuración en *__logback-spring.xml__*


## Funcionamiento Automático

- Automatización con JobRunr: 
Cada minuto, el programa ingresa un Player nuevo con un tiempo disponible el cual efectúa una serie de apuestas.
Cada vez que ingresa un nuevo Player se purga cualquier Player que estuviera con tiempo caducado (para evitar que crezca el consumo de memoria).
Se puede observar el devenir de los trabajos de JobRunr en http://localhost:8000/, el cual se instala por defecto.
Cada transacción se muestra en el log.


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
