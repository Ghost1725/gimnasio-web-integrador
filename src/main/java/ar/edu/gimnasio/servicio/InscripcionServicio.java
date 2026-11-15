package ar.edu.gimnasio.servicio;

import ar.edu.gimnasio.dtos.ActividadVista;
import ar.edu.gimnasio.dtos.InscripcionVista;
import ar.edu.gimnasio.dtos.SocioVista;
import ar.edu.gimnasio.excepcion.ReglaNegocioException;
import ar.edu.gimnasio.repositorio.ActividadRepositorio;
import ar.edu.gimnasio.repositorio.InscripcionRepositorio;
import ar.edu.gimnasio.repositorio.SocioRepositorio;
import java.util.List;
import org.springframework.stereotype.Service;

/** Operacion del negocio adicional al ABM: registrar y cancelar inscripciones. */
@Service
public class InscripcionServicio {
    private final InscripcionRepositorio inscripciones;
    private final SocioRepositorio socios;
    private final ActividadRepositorio actividades;

    public InscripcionServicio(InscripcionRepositorio inscripciones, SocioRepositorio socios,
                              ActividadRepositorio actividades) {
        this.inscripciones = inscripciones;
        this.socios = socios;
        this.actividades = actividades;
    }

    public List<InscripcionVista> listar(Long actividadId) {
        return inscripciones.todas().stream()
                .filter(i -> actividadId == null || i.actividadId().equals(actividadId))
                .toList();
    }

    // synchronized protege registros simultaneos dentro de una sola instancia de esta aplicacion.
    public synchronized void registrar(Long socioId, Long actividadId) {
        if (socioId == null || actividadId == null) {
            throw new ReglaNegocioException("Selecciona un socio y una actividad.");
        }
        SocioVista socio = socios.buscar(socioId);
        ActividadVista actividad = actividades.buscar(actividadId);
        if (!socio.activo()) throw new ReglaNegocioException("El socio esta inactivo.");
        if (!actividad.activa()) throw new ReglaNegocioException("La actividad esta inactiva.");

        List<InscripcionVista> existentes = inscripciones.todas();
        boolean duplicada = existentes.stream().anyMatch(i -> i.estaActiva()
                && i.socioId().equals(socioId) && i.actividadId().equals(actividadId));
        if (duplicada) throw new ReglaNegocioException("Este socio ya esta inscripto en esa actividad.");

        long ocupados = existentes.stream().filter(i -> i.estaActiva()
                && i.actividadId().equals(actividadId)).count();
        if (ocupados >= actividad.cupo()) {
            throw new ReglaNegocioException("La actividad no tiene cupos disponibles.");
        }
        inscripciones.registrar(socioId, actividadId);
    }

    public synchronized void cancelar(Long id) {
        InscripcionVista i = inscripciones.todas().stream()
                .filter(v -> v.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new ReglaNegocioException("No existe esa inscripcion."));
        if (!i.estaActiva()) throw new ReglaNegocioException("Esa inscripcion ya esta cancelada.");
        inscripciones.cancelar(id);
    }
}
