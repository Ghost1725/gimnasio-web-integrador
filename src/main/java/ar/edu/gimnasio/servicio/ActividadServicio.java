package ar.edu.gimnasio.servicio;

import ar.edu.gimnasio.dtos.ActividadFormulario;
import ar.edu.gimnasio.dtos.ActividadResumen;
import ar.edu.gimnasio.dtos.ActividadVista;
import ar.edu.gimnasio.dtos.InscripcionVista;
import ar.edu.gimnasio.excepcion.ReglaNegocioException;
import ar.edu.gimnasio.repositorio.ActividadRepositorio;
import ar.edu.gimnasio.repositorio.InscripcionRepositorio;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;

@Service
public class ActividadServicio {
    private final ActividadRepositorio actividades;
    private final InscripcionRepositorio inscripciones;

    public ActividadServicio(ActividadRepositorio actividades, InscripcionRepositorio inscripciones) {
        this.actividades = actividades;
        this.inscripciones = inscripciones;
    }

    public List<ActividadResumen> listar(String filtro) {
        String q = filtro == null ? "" : filtro.trim().toLowerCase(Locale.ROOT);
        List<InscripcionVista> inscriptos = inscripciones.todas();
        return actividades.todas().stream()
                .filter(a -> a.nombre().toLowerCase(Locale.ROOT).contains(q)
                        || a.dia().toLowerCase(Locale.ROOT).contains(q))
                .map(a -> new ActividadResumen(a, inscriptos.stream()
                        .filter(i -> i.estaActiva() && i.actividadId().equals(a.id()))
                        .count()))
                .toList();
    }

    public ActividadVista buscar(Long id) { return actividades.buscar(id); }

    public void guardar(ActividadFormulario f) {
        f.setNombre(f.getNombre() == null ? "" : f.getNombre().trim());
        f.setDescripcion(f.getDescripcion() == null ? "" : f.getDescripcion().trim());
        f.setDia(f.getDia() == null ? "" : f.getDia().trim());
        f.setHora(f.getHora() == null ? "" : f.getHora().trim());
        if (f.getNombre().isBlank() || f.getDia().isBlank() || f.getHora().isBlank()
                || f.getCupo() == null || f.getCupo() < 1) {
            throw new ReglaNegocioException("Completa el nombre, dia, hora y un cupo mayor a cero.");
        }
        if (f.getId() != null) {
            long ocupados = inscripciones.todas().stream()
                    .filter(i -> i.estaActiva() && i.actividadId().equals(f.getId()))
                    .count();
            if (f.getCupo() < ocupados) {
                throw new ReglaNegocioException("No podes reducir el cupo por debajo de las "
                        + ocupados + " inscripciones activas.");
            }
        }
        actividades.guardar(f);
    }

    public void eliminar(Long id) {
        if (inscripciones.todas().stream().anyMatch(i -> i.actividadId().equals(id))) {
            throw new ReglaNegocioException("No se puede eliminar una actividad con inscripciones. "
                    + "Podes marcarla como inactiva.");
        }
        actividades.eliminar(id);
    }
}
