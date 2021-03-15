# Universidad virtual para proveedores externos
Proyecto para desarrollar módulos de Universidad Virtual por proveedores externos.

Acronimo BD: 

paquete java: 

## 1. Método de trabajo
### Código fuente en GIT
Todo el código se deposita en gitujap.ujaen.es en las ramas asignada al proyecto.

Los proyectos tendrán dos ramas pricipales, una para desarrollo y otra para preproducción. Los desarrolladores podrán crear ramas a partir de estas.

- El entorno de desarrollo servirá para que el servicio de Informática valida el código.
- El entorno de preproducción servirá para que los responsables funcionales prueben la aplicación.

No se tendrá acceso a la rama master, no a entorno de producción.

Los ejemplos, librerias u otro tipo de recursos se facilitarán en git. Todo el intercambio de información se realizará en la plataforma de gitujap.ujaen.es

En caso de ser necesario, se crearán incidencias en git o comentarios en los pull request.

#### Pasos para desplegar la aplicación en producción
1. Pull request a rama del proyecto de desarrollo. Asignar a contacto técnico UJA.
2. Validación por parte del servicio de informática.
3. Pull request a rama del proyecto de preproducción. Asignar a responsable técnico UJA.
4. Validación por parte de los usuarios funcionales.
5. Una vez obtenido el visto bueno del servicio de informatica y de los usuarios funcionales, se desplegará en producción.

## 2. Desarrollo de la aplicación web
### Requerimientos
Se necesita el siguiente software
- [Eclipse](https://www.eclipse.org/)
- Java jdk 11 de [AdpotJdk](https://adoptopenjdk.net/)
- [tomcat 9](http://tomcat.apache.org/)
- para ejecutarlo en contenedores [docker](https://www.docker.com/) y docker-compose

#### Preparación de proyecto en eclipse
- File -> import -> proyect from git. Lo cojemos de gitujap.ujaen.es
- Creamos servidor de tomcat. 
Doble click -> open lauch configuracion -> arguments -> VM arguments
 -DdbUrl=<ip de la db> -DmemcacheUrl=<ip de memcache>
- Incluimos el proyecto uv-externo en el servidor tomcat
- Doble click en tomcat -> modules -> edit. El path debe ser /
- Para el proyecto tiene que estar activo propiedades -> Java Compiler -> Error/Warning -> Enable '@SuppressWarnings' annotations
- Incluir en build path las librerias junit v4 que proporciona eclipse 
- Incluir en el buid path las librerias de selenium de Documentos/selenium y Documentos/selenium/libs

### Contrucción del war
ejecutar ant war.
Si estas en eclipse, botón derecho sobre build.xml -> run as ant build

### Arrancar el servidor
Para probar la aplicación debe correr sobre un servidor tomcat

#### Entorno docker
1. Construir imagen docker de [oracle database](https://github.com/oracle/docker-images/tree/master/OracleDatabase/SingleInstance)
2. asegurarse de haber generado el war
3. ejecutar docker-compose up --build

#### Entorno eclipse
1. arrancar el servidor oracle
2. arrancar el servidor memcache
3. arrancar el servidor tomcat

### Navegar por aplicación web
abrir navegador y poner el enlace del servidor tomcat. Por ejemplo http://127.0.0.1:8080
Para iniciar sesión con un usuario pulsar arriba a la derecha en iniciar sesión.

## 3. Recursos a generar
El módulo de Uvirtual recibirá un nombre y un alias para base de datos. Por ejemplo, Proyecto bolsa de empleo para PDI. Nombre: bolsaEmpleo. Acrónimo para base de datos: BEP
Cada módulo tiene que tener los siguientes componentes:

Todos los recursos generados seguiran la [guia de estilo](STYLE.md).

### Código fuente en directorio src
Como mínimo, se debe generar modelo, vista y controlador

### pruebas en directorio test
Como mínimo, se debe generar test unitarios, test de controlador y test de usuario.
El conjunto de test debe cubrir al menos el 80% del código 

### Documentación en directorio Documentos
Como minimo, se debe generar los scripts de base de datos 
Los identificadores de opciones, roles, etc que se vayan a crear se solicitarán al Servicio de informática de la UJA.
