# Propuesta del proyecto - entregar en Google Docs

**Nombre:** GimnasioWeb - Sistema web de gestión de socios y actividades.

**Integrantes:**
- [COMPLETAR: Nombre y apellido 1]
- [COMPLETAR: Nombre y apellido 2]
- [OPCIONAL: Nombre y apellido 3]
- [OPCIONAL: Nombre y apellido 4]

## Problema que resuelve

En un gimnasio, llevar el registro de socios, clases y cupos en planillas aisladas puede
producir inscripciones repetidas y exceder la capacidad de una actividad. GimnasioWeb
centraliza la información y facilita la inscripción de socios a actividades disponibles.

## Entidades y relaciones

- **Socio:** ID, nombre, apellido, DNI, correo, teléfono y estado.
- **Actividad:** ID, nombre, descripción, día, horario, cupo y estado.
- **Inscripción:** ID, socio asociado, actividad asociada, fecha y estado.
- **Relaciones 1:N:** un socio puede tener muchas inscripciones; una actividad puede tener
  muchas inscripciones. Cada inscripción pertenece a exactamente un socio y una actividad.

## Funcionalidades

1. ABM de socios: registrar, listar, editar y eliminar cuando no tengan historial.
2. ABM de actividades: registrar, listar, editar y eliminar cuando no tengan inscripciones.
3. Consulta de inscripciones e historial de cancelaciones.
4. Búsqueda de socios por nombre, apellido o DNI y de actividades por nombre o día.
5. Filtro de inscripciones por actividad.
6. Inscribir un socio activo a una actividad activa, controlando cupo y evitando duplicados.
7. Cancelar inscripciones y liberar cupos sin borrar el historial.
8. Mensajes de validación, confirmación y errores claros.

## Tecnologías

Java 17, Maven, Spring Boot, Spring MVC, HTML, CSS, Bootstrap, Thymeleaf,
DataNucleus JDO y MySQL 8.

**Importante:** copiar este texto a un documento real de Google Docs, compartirlo con
los permisos que pida la materia y pegar el enlace en la planilla de grupos.
