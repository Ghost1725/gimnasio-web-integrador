# Guía para explicar el proyecto en la presentación y defensa

## 1. Presentación breve (30-45 segundos)

"Nuestro proyecto se llama GimnasioWeb. Es una página web que permite registrar
socios, organizar las actividades del gimnasio e inscribir personas respetando
los cupos. Elegimos esta temática porque llevar los registros manualmente puede
provocar errores, por ejemplo anotar dos veces a un socio o superar la capacidad
máxima de una clase. El sistema se conecta a MySQL y mantiene la información
incluso después de cerrar la aplicación."

## 2. Entidades del modelo de dominio

- **Socio:** representa a la persona que se anota al gimnasio. Tiene identificador,
  nombre, apellido, DNI, correo, teléfono y estado activo/inactivo.
- **Actividad:** representa una clase, por ejemplo yoga. Tiene identificador,
  nombre, descripción, día, hora, cupo y estado.
- **Inscripción:** es el vínculo entre un socio y una actividad. Registra
  la fecha y si está activa o cancelada.

**Relaciones:** un socio tiene **muchas** inscripciones (1:N); una actividad
puede tener **muchas** inscripciones (1:N). Cada inscripción apunta a un solo
socio y a una sola actividad (N:1).

Ejemplo: Ana participa en Yoga y Spinning. Son dos inscripciones distintas,
ambas pertenecen a Ana pero cada una apunta a una actividad diferente.

## 3. ¿Qué hace cada capa?

| Capa | Responsabilidad | Ejemplo |
|---|---|---|
| Dominio | Representa la información | `Socio`, `Actividad`, `Inscripcion` |
| Repositorio | Consulta y guarda datos usando JDO | `InscripcionRepositorio` |
| Servicio | Decide si la operación está permitida | `InscripcionServicio` revisa el cupo |
| Controlador | Recibe peticiones del navegador y decide qué vista mostrar | `InscripcionesControlador` |
| Vista | Presenta formularios y listados con HTML y Thymeleaf | `inscripciones/formulario.html` |

**Spring** instancia esas clases e **inyecta dependencias por constructor**:
por ejemplo, le entrega un `InscripcionServicio` al controlador sin que tengamos
que crearlo con `new`.

## 4. Explicación de las tecnologías

**Maven:** usa `pom.xml` para descargar dependencias, ejecutar pruebas,
compilar y empaquetar. También invoca el plugin DataNucleus para hacer
`enhancement` de las clases JDO.

**Spring Boot:** inicia la aplicación web y el servidor integrado.

**MVC:** separa el **Model** (datos enviados desde el controlador), la
**View** (HTML/Thymeleaf) y el **Controller** (maneja las rutas HTTP).

**Thymeleaf:** inserta datos del servidor en HTML. Ejemplo:
`th:each="s : ${socios}"` repite una fila por socio y `th:text="${s.dni}"`
coloca el DNI en la tabla.

**HTML, CSS y Bootstrap:** construyen la interfaz, estilos y diseño adaptable.

**JDO:** es una API para guardar objetos Java sin escribir SQL para cada ABM.

**DataNucleus:** implementa JDO, crea/mapea las tablas en MySQL y mantiene
las relaciones entre las entidades.

**MySQL:** conserva datos de forma permanente.

## 5. Recorrido completo: registrar una inscripción

1. El usuario abre `/inscripciones/nueva`.
2. `InscripcionesControlador.nueva()` obtiene socios y actividades y envía
   listas al `Model`.
3. Thymeleaf arma el formulario con esas listas.
4. El usuario elige socio y actividad y confirma.
5. El navegador envía un `POST` a `/inscripciones/guardar`.
6. El controlador verifica los campos y llama a `InscripcionServicio.registrar()`.
7. El servicio revisa que ambos estén activos, que no exista una inscripción
   activa repetida y que la cantidad sea menor al cupo.
8. Si pasa todas las validaciones, llama a `InscripcionRepositorio.registrar()`.
9. El repositorio abre un `PersistenceManager`, comienza una transacción,
   recupera los objetos JDO y persiste una nueva `Inscripcion`.
10. La transacción termina con `commit`; ante un error hace `rollback`.
    Finalmente se cierra el `PersistenceManager`.
11. El controlador redirige a la lista y la pantalla confirma la inscripción.

## 6. Demostración en vivo

Primero crear un socio llamado Ana, luego uno llamado Luis. Crear una actividad
Yoga con cupo 1. Inscribir a Ana y mostrar que quedan 0 cupos. Intentar inscribir
a Luis: debe dar un error. Intentar inscribir a Ana de nuevo: error de duplicado.
Cancelar la inscripción de Ana: ahora queda 1 cupo. Inscribir a Luis: funciona.
Filtrar inscripciones por actividad, editar un socio y tratar de borrar la
actividad con inscripciones: la app impide dejar relaciones inconsistentes.
Finalmente detener y volver a iniciar la app: los datos siguen en MySQL.

## 7. Preguntas típicas y cómo responderlas

**¿Qué es un ABM?** Alta, baja y modificación de datos. En la aplicación
además hay listados (también llamado CRUD: create, read, update, delete).

**¿Por qué tres entidades?** Porque necesitamos saber quién es el socio,
qué actividad existe y cuál es la relación concreta entre ellos.

**¿Por qué la inscripción es una entidad y no solo una lista?** Porque contiene
información propia: identificador, fecha y estado, y permite guardar un historial.

**¿Qué pasa si se llena un cupo?** El servicio compara inscripciones activas
contra el máximo de la actividad; si ya está lleno, lanza una excepción de
regla de negocio y no guarda la inscripción.

**¿Qué pasa con las eliminaciones relacionadas?** Se bloquean para no perder
inscripciones históricas; se puede desactivar al socio o la actividad.

**¿Qué significa `@PersistenceCapable`?** Que la clase está preparada para
persistirse mediante JDO/DataNucleus.

**¿Qué es una transacción?** Un conjunto de cambios que se guarda junto con
`commit` o se revierte con `rollback` si ocurrió un error.

**¿Por qué un `PersistenceManager` por operación?** Para abrir y cerrar
correctamente la sesión que accede a MySQL. La fábrica de managers se comparte.

**¿Se usa JDBC de forma directa?** No en los repositorios de la app: JDO usa
el driver de MySQL por debajo, pero el proyecto manipula objetos persistentes.

**¿Por qué importa el enhancement?** DataNucleus transforma los `.class` de
las entidades para que puedan monitorear cambios y trabajar con persistencia.

## 8. Reparto real de tareas entre el grupo

Completar según lo que realmente hizo cada persona. Ejemplo de distribución:

- Integrante 1: dominio, asociaciones y `ConfiguracionJdo`.
- Integrante 2: repositorios, transacciones y consultas.
- Integrante 3 (si corresponde): controladores, servicios y validaciones.
- Integrante 4 (si corresponde): vistas HTML, CSS, Bootstrap, documentación y pruebas.

**No presentar una división ficticia:** todos tienen que explicar el sistema
completo, además de su aporte individual.

## 9. Verificaciones antes de entregar

- [ ] Pegar nombres y apellidos reales en `README.md` y en la propuesta.
- [ ] Crear Google Docs y publicar el enlace en la planilla de grupos.
- [ ] Ejecutar `mvn clean package` y `mvn test` sin errores.
- [ ] Crear la base con `sql/01-crear-base.sql`.
- [ ] Probar ABM, búsqueda, filtro, inscripciones, cupos y cancelaciones.
- [ ] Comprobar que la información sigue después de reiniciar.
- [ ] Subir el código completo a GitHub sin la contraseña de MySQL.
- [ ] Probar el README en otro equipo si es posible.
- [ ] Practicar el recorrido de una inscripción desde la pantalla hasta MySQL.
