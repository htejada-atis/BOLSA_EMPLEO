# Guia de estilo de desarrollo externo de Aplicaciones
 
## INTRODUCCIÓN
A continuación se define la guía de estilos a usar en el proyecto. No es una guía cerrada, por lo que analizaremos las propuestas que nos plantéis y en su caso, se incorporarán a ella.

## ACRÓNIMO DEL PROYECTO
El acrónimo del proyecto estará formado por tres caracteres alfabéticos y lo definirá y comunicará el Servicio de Informática.

Por ejemplo: BEP (lo usaremos en todo este documento)

## OBJETOS DE BASES DE DATOS
### TABLAS Y CAMPOS
La denominación de las tablas se iniciará con T y a continuación, el acrónimo del proyecto seguido de _ y se añadirá texto relevante relacionado con su contenido.

A la tabla se le asignará un alias que comience por el acrónimo del proyecto y tres caracteres más, significativos y elegidos de entre los que componen el nombre de la tabla. Será único y se usará en todas las sentencias SQL en las que figure la tabla. Los tres caracteres últimos se usarán en la denominación de índices, restricciones (constraints) y demás objetos relacionados con la tabla (sólo estos tres porque estos objetos contendrán en su denominación referencia al proyecto).  Este alias se indicará en el comentario de la tabla.

Asimismo, los nombres de los campos deberían ser significativos.

Los campos índice únicos se denominarán CODNUM o CODALF, en función de que sea numérico o alfabético.

Los campos que formen parte de una clave externa se iniciarán por el alias de la tabla externa, a continuación el carácter _ y tras esto, la denominación de ese campo en dicha.

Tanto la tabla como sus campos deberían disponer de comentario sobre su funcionalidad (cláusulas COMMENT ON TABLE, COMMENT ON COLUMN).

Como idea, los campos que tengan valor S, N, etc., podrían contener las siglas FLG (por ejemplo, FLGACTIVO).

Por ejemplo, la tabla empleado, podría denominarse TBEP_EMPLEADO, alias bepemp y caracteres significativos EMP (que se usarán en índices, restricciones, etc).

Otros ejemplos:
- SELECT … FROM tbep_empleado bepemp WHERE bepemp.nombreCampo ….
- Nombre campo clave principal: CODNUM
- Nombre campo clave externa con tabla solicitudes: BEPSOL_CODNUM

#### Tablas históricas o auditoría
Estas tablas comenzarán por TBEP_HTO_ y a continuación la denominación de la tabla de la que almacenan el histórico de movimientos o cambios.

Por ejemplo: TBEP_HTO_EMPLEADO

### ÍNDICES
La denominación de los índices se iniciará con I más el acrónimo del proyecto seguido de _ , a continuación P, F o U, en función de que sea clave primaria, externa (foreign) o única, después los tres últimos caracteres del alias designado para la tabla y en el caso de que sea externa, el carácter _ y los tres últimos caracteres del alias designado para la tabla externa.

Por ejemplo, clave principal de la tabla de empleado IBEP_PEMP y clave externa con tabla de solicitudes: IBEP_FEMP_SOL

### RESTRICCIONES
La denominación de las restricciones se iniciará con R más el acrónimo del proyecto seguido de _  , a continuación N, C, R, en función de que sea no nula, de comprobación (check) o Ref constraint, después los tres últimos caracteres del alias designado para la tabla, tras ello _ y el nombre del campo al que afecta.

Por ejemplo: RBEP_NEMP_APELLIDO1

### TRIGGERS O DISPARADORES
La denominación de los disparadores se iniciará con D más el acrónimo del proyecto seguido de _  , después los tres últimos caracteres del alias designado para la tabla, tras ello _ y dos caracteres AR, AS, BR, BS según el tipo after each row, after statement, before each row, before statemen, respetivamente y a continuación el carácter correspondiente a cada evento que lo dispara I, U, D, correspondientes a insert, update, delete.

Por ejemplo: DBEP_EMP_ARIUD

### VISTAS
La denominación de las vistas se iniciará con V más el acrónimo del proyecto BEP seguido de _ y nombre significativo.

Es decir, comenzarán por VBEP_

### VISTAS MATERIALIZADAS
La denominación de estas vistas se iniciará por M y a continuación el acrónimo del proyecto seguido de _ y nombre significativo.

Es decir, comenzarán por MBEP_

Ejemplo: MBEP_ESTADISTICAS

### SINÓNIMOS
La denominación de los sinónimos se iniciará por S y a continuación el acrónimo del proyecto seguido de _ y el nombre significativo del objeto que referencia.

Es decir, comenzarán por SBEP_

Ejemplo: SBEP_SOLICITUDES

### SECUENCIAS
La denominación de las secuencias se iniciará por Q y a continuación el acrónimo del proyecto seguido de _  , los tres últimos caracteres del alias designado para la tabla en la que se usa. En caso de que use más de una secuencia en la misma taba, podrá añadir _ y algún nombre significativo relacionada con el campo relacionado.

Es decir, comenzarán por QBEP_

Ejemplo: QBEP_EMP

### PAQUETES
Las funciones y procedimientos que se usen en la aplicación se incluirán en paquetes.

La denominación de los paquetes comenzará por K , a continuación el acrónimo del proyecto seguido de _ y la denominación del paquete.

Por ejemplo: KBEP_GESTION_SOLICITUDES

## JAVA
El código java debe estar formateado siguiendo las reglas definidas en el fichero checkstyle.xml y los ejemplos proporcionados.
Para el proyecto tiene que estar activo propiedades -> Java Compiler -> Error/Warning -> Enable '@SuppressWarnings' annotations

Si surge alguna duda, se seguiran las recomendaciones de 
1. [JavaSE](https://www.oracle.com/java/technologies/javase/codeconventions-contents.html)
2. [Google](https://google.github.io/styleguide/javaguide.html)

El código no debe tener errores o warnings (incluir nuevos warnings o errores en el código).

El código no debe tener bloques duplicados (copiar y pegar).

El código generado debe ser seguro. Seguirá las recomentaciones de [Oracle](https://www.oracle.com/java/technologies/javase/seccodeguide.html) y [SEI CERT Oracle Coding Standard for Java](https://wiki.sei.cmu.edu/confluence/display/java/SEI+CERT+Oracle+Coding+Standard+for+Java)

Se seguirán buenas prácticas de programación de [OWASP](https://owasp.org/), prestando atención a no incliur [CWE](http://cwe.mitre.org/index.html)