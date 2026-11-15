package ar.edu.gimnasio.repositorio;

import ar.edu.gimnasio.config.UnidadDeTrabajoJdo;
import ar.edu.gimnasio.dominio.Actividad;
import ar.edu.gimnasio.dominio.Inscripcion;
import ar.edu.gimnasio.dominio.Socio;
import ar.edu.gimnasio.dtos.InscripcionVista;
import ar.edu.gimnasio.excepcion.ReglaNegocioException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import javax.jdo.JDOObjectNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class InscripcionRepositorio {
    private final UnidadDeTrabajoJdo jdo;

    public InscripcionRepositorio(UnidadDeTrabajoJdo jdo) { this.jdo = jdo; }

    public List<InscripcionVista> todas() {
        return jdo.leer(pm -> ConsultasJdo.todos(pm, Inscripcion.class).stream()
                .map(InscripcionVista::desde)
                .sorted(Comparator.comparing(InscripcionVista::fecha).reversed()
                        .thenComparing(InscripcionVista::id, Comparator.reverseOrder()))
                .toList());
    }

    public void registrar(Long socioId, Long actividadId) {
        jdo.escribir(pm -> {
            Socio socio = SocioRepositorio.obtener(pm, socioId);
            Actividad actividad = ActividadRepositorio.obtener(pm, actividadId);
            Inscripcion i = new Inscripcion(socio, actividad, LocalDate.now());
            // Se mantienen ambos extremos de las relaciones bidireccionales.
            socio.agregarInscripcion(i);
            actividad.agregarInscripcion(i);
            pm.makePersistent(i);
            return null;
        });
    }

    public void cancelar(Long id) {
        jdo.escribir(pm -> {
            try {
                Inscripcion inscripcion = pm.getObjectById(Inscripcion.class, id);
                inscripcion.cancelar();
            } catch (JDOObjectNotFoundException e) {
                throw new ReglaNegocioException("No existe la inscripcion solicitada.");
            }
            return null;
        });
    }
}
