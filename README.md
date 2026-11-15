# GimnasioWeb - Proyecto Integrador Grupal

Aplicación web para gestionar **socios, actividades e inscripciones**, desarrollada para
cumplir la consigna del proyecto integrador. Usa **Java 17, Maven, Spring Boot 3.5,
Spring MVC, Thymeleaf, Bootstrap, DataNucleus JDO y MySQL**. **No usa JPA/Hibernate**:
la persistencia de las tres entidades se realiza con JDO.

## Integrantes

- **Integrante 1:** [COMPLETAR]
- **Integrante 2:** [COMPLETAR]
- Integrante 3 (opcional): [COMPLETAR]
- Integrante 4 (opcional): [COMPLETAR]

## Qué hace el sistema

- **Socios:** alta, listado, búsqueda por nombre/apellido/DNI, edición, baja condicionada,
  activación e inactivación.
- **Actividades:** alta, listado, búsqueda por nombre/día, edición, baja condicionada,
  cupo total y disponible, activación e inactivación.
- **Inscripciones:** alta vinculando socio + actividad, listado, filtro por actividad,
  cancelación y conservación de historial.
- **Reglas de negocio:** no inscribe a socios inactivos, no inscribe a actividades
  inactivas, no permite una segunda inscripción activa del mismo socio a la misma
  actividad, no supera el cupo; tampoco permite reducir el cupo por debajo de los
  inscriptos. Las bajas de socios/actividades se impiden si tienen inscripciones
  (también canceladas).
- **Validaciones:** campos obligatorios, formato del email, DNI numérico de 7 a 10
  dígitos, horario HH:mm, cupo positivo, mensajes de éxito y error.

## Modelo de datos

```text
SOCIO (1) -------- (N) INSCRIPCION (N) -------- (1) ACTIVIDAD
 id  PK                   id PK                          id PK
 nombre                   socio -> FK                    nombre
 apellido                 actividad -> FK                descripcion
 dni                      fecha                          dia
 email                    estado                         hora
 telefono                                                cupo
 activo                                                  activa
```

**Relaciones:** `Socio.inscripciones` y `Actividad.inscripciones` son colecciones
`@Persistent(mappedBy=...)`. `Inscripcion.socio` e `Inscripcion.actividad` son referencias
JDO a sus entidades. Cada registro de inscripción contiene su fecha y estado.

## Requisitos para instalarlo en Windows

1. **JDK 17 o 21** instalado. Comprobar con `java -version`.
2. **Apache Maven 3.9+** en el PATH. Comprobar con `mvn -version`.
3. **MySQL Server 8** en ejecución y **MySQL Workbench** (Workbench es opcional,
   pero facilita los pasos). La base y las tablas se guardan en MySQL, no en memoria.
4. Acceso a Internet la primera vez para que Maven descargue las dependencias;
   Bootstrap se carga desde CDN, pero también hay estilos CSS propios.

> Si usás JDK 21 no necesitás instalar un segundo JDK: el proyecto compila con
> nivel Java 17. Asegurate de que Maven apunte al JDK correcto (`mvn -version`).

## Paso 1. Crear la base de datos

Abrí MySQL Workbench, conectate al servidor, ejecutá el archivo
[`sql/01-crear-base.sql`](sql/01-crear-base.sql) (icono del rayo) o escribí:

```sql
CREATE DATABASE IF NOT EXISTS gimnasio_web
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

**No es necesario escribir a mano las tablas.** DataNucleus las crea cuando la aplicación
accede a cada entidad gracias a `datanucleus.schema.autoCreateAll=true`.

**Atención:** usar una base vacía llamada `gimnasio_web` para evitar conflictos con
un proyecto anterior de Swing/JDBC que tuviera tablas distintas. No se importa ni
se borran automáticamente los datos de aquel proyecto.

## Paso 2. Configurar credenciales sin subir contraseñas a GitHub

En PowerShell de Windows, dentro de la carpeta que contiene `pom.xml`, escribí:

```powershell
$env:DB_USER="root"
$env:DB_PASSWORD="TU_CLAVE_DE_MYSQL"
```

Si la contraseña es vacía, `DB_PASSWORD` puede quedar vacío. Si el servidor usa
otro puerto o nombre de base, opcionalmente definí:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/gimnasio_web?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Argentina/Buenos_Aires"
```

Las variables de entorno solo se aplican a esa ventana de PowerShell. **Nunca
escribas tu clave real en `README.md`, `pom.xml` ni `application.properties`.**

## Paso 3. Compilar y ejecutar

Descomprimí el ZIP, abrí PowerShell dentro de la carpeta `gimnasio-web-integrador`
(justo donde está `pom.xml`) y ejecutá:

```powershell
mvn clean package
mvn spring-boot:run
```

En la primera ejecución Maven descargará bibliotecas de Internet. El plugin de
DataNucleus hace automáticamente el **enhancement** de las clases persistentes
antes del empaquetado (fase `process-classes`). Si querés ejecutarlo manualmente:

```powershell
mvn datanucleus:enhance
```

Alternativamente, después de `mvn clean package`:

```powershell
java -jar target/gimnasio-web-integrador-1.0.0.jar
```

Abrí el navegador en **http://localhost:8080**.

## Paso 4. Probar el sistema de principio a fin

1. En **Socios**, registrá a Ana Pérez (DNI 12345678) y a Luis Gómez (DNI 23456789).
2. En **Actividades**, creá Yoga: lunes 18:00, cupo **1**, activa.
3. En **Inscripciones**, inscribí a Ana en Yoga. Debe indicar éxito; cupos libres: **0**.
4. Intentá inscribir a Luis en Yoga. Debe impedirlo: **sin cupos**.
5. Intentá inscribir nuevamente a Ana. Debe impedirlo: **inscripción duplicada**.
6. Cancelá la inscripción de Ana. La fila histórica queda con estado CANCELADA y
   el cupo vuelve a **1**.
7. Ahora inscribí a Luis. Debe permitirlo.
8. Intentá bajar el cupo de Yoga a cero o borrar Yoga: debe impedirlo.
9. Intentá eliminar a Ana: como tiene una inscripción cancelada, debe impedirlo
   para no perder el historial. Podés marcarla inactiva.
10. Cerrá el programa con `Ctrl+C` y volvé a ejecutar. Los datos deben seguir ahí.

## Consultar MySQL para la demostración

En una pestaña nueva de Workbench (cuando ya registraste datos):

```sql
USE gimnasio_web;
SHOW TABLES;
SELECT * FROM SOCIO;
SELECT * FROM ACTIVIDAD;
SELECT * FROM INSCRIPCION;
```

Las columnas de las relaciones se generan a partir del modelo JDO. Si tu versión
de MySQL/DataNucleus usa identificadores distintos, mirá `DESCRIBE INSCRIPCION;`.

## Organización del código (MVC + capas)

```text
src/main/java/ar/edu/gimnasio/
  GimnasioApplication.java      -> arranque de Spring Boot
  dominio/                       -> Socio, Actividad, Inscripcion (entidades JDO)
  repositorio/                   -> consultas y transacciones con MySQL vía JDO
  servicio/                      -> validaciones y reglas del negocio
  controlador/                   -> solicitudes HTTP, Model y navegación
  dtos/                          -> formularios validados y datos para vistas
  config/                        -> PMF, apertura/cierre de PM y transacciones
src/main/resources/
  templates/                     -> pantallas HTML dinámicas con Thymeleaf
  static/css/estilos.css         -> diseño CSS
  application.properties         -> configuración por variables de entorno
src/test/                        -> pruebas unitarias de reglas de negocio
sql/01-crear-base.sql            -> preparación de la base de datos
```

**Ejemplo de recorrido:** al confirmar una inscripción, el navegador hace
`POST /inscripciones/guardar`. `InscripcionesControlador` recibe el formulario,
`InscripcionServicio` valida estado/cupo/duplicados, `InscripcionRepositorio`
recupera socio y actividad con un `PersistenceManager`, persiste `Inscripcion` y
confirma la transacción MySQL. El controlador redirige al listado y Thymeleaf
muestra los datos del `Model`.

## Pruebas automatizadas

```powershell
mvn test
```

Hay pruebas JUnit/Mockito para: alta con cupo, duplicado, cupo agotado, socio
inactivo, cancelar y volver a inscribir, DNI repetido, eliminación con historial
y reducción de cupo indebida. **Estas son pruebas unitarias de servicios**;
además se requiere la prueba manual integral con un MySQL levantado.

## GitHub y entrega

1. Creá un **repositorio vacío** en GitHub, por ejemplo `gimnasio-web-integrador`.
2. Abrí Git Bash en esta carpeta:

```bash
git init
git add .
git commit -m "Proyecto integrador gimnasio web"
git branch -M main
git remote add origin https://github.com/TU_USUARIO/gimnasio-web-integrador.git
git push -u origin main
```

3. Sustituí `TU_USUARIO` por el nombre real de la cuenta (no se puede inventar).
4. Pegá en la entrega el enlace del repositorio publicado.
5. Copiá el texto de [`PROPUESTA_PARA_GOOGLE_DOCS.md`](PROPUESTA_PARA_GOOGLE_DOCS.md)
   a Google Docs y compartí el enlace en la planilla de grupos.
6. Leé [`GUIA_DEFENSA.md`](GUIA_DEFENSA.md) para preparar la exposición de cada
   integrante. Anotá allí quién implementó cada parte (no inventar aportes).

## Solución de problemas

- **`mvn` no se reconoce:** agregar `C:\apache-maven...\bin` a Path y abrir otra terminal.
- **`Access denied for user 'root'`:** la contraseña de `DB_PASSWORD` es incorrecta.
- **`Communications link failure`:** verificar que el servicio MySQL esté iniciado,
  puerto 3306 y dirección de conexión.
- **`Unknown database 'gimnasio_web'`:** ejecutar primero el archivo SQL.
- **`ClassNotPersistenceCapableException`:** usar `mvn clean package` (no solamente
  compilar manualmente con `javac`): el enhancement JDO es obligatorio.
- **Puerto 8080 ocupado:** configurar en PowerShell `$env:PORT="8081"` y abrir
  http://localhost:8081.
- **Errores al crear tablas:** verificar permisos `CREATE/ALTER` de la cuenta SQL.

## Consideraciones

- El control `synchronized` del servicio de inscripción evita dos inscripciones
  simultáneas en **una sola instancia** de la aplicación; para producción con
  varias instancias habría que añadir bloqueo transaccional/constraints en BD.
- El proyecto es académico; no trae usuarios, autenticación, backups ni
  controles de producción. Para publicarlo en Internet, hay que añadir seguridad.
- **Versión del código:** propuesta académica lista para validar en un equipo
  con Maven y MySQL. El empaquetado ZIP no implica una ejecución real verificada.
