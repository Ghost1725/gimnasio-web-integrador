package ar.edu.gimnasio.repositorio;

import ar.edu.gimnasio.config.UnidadDeTrabajoJdo;
import ar.edu.gimnasio.dominio.Actividad;
import ar.edu.gimnasio.dtos.ActividadFormulario;
import ar.edu.gimnasio.dtos.ActividadVista;
import ar.edu.gimnasio.excepcion.ReglaNegocioException;
import java.util.Comparator;
import java.util.List;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import org.springframework.stereotype.Repository;

@Repository
public class ActividadRepositorio {
    private final UnidadDeTrabajoJdo jdo;

    public ActividadRepositorio(UnidadDeTrabajoJdo jdo) { this.jdo = jdo; }

    public List<ActividadVista> todas() {
        return jdo.leer(pm -> ConsultasJdo.todos(pm, Actividad.class).stream()
                .map(ActividadVista::desde)
                .sorted(Comparator.comparing(ActividadVista::nombre, String.CASE_INSENSITIVE_ORDER))
                .toList());
    }

    public ActividadVista buscar(Long id) {
        return jdo.leer(pm -> ActividadVista.desde(obtener(pm, id)));
    }

    public void guardar(ActividadFormulario f) {
        jdo.escribir(pm -> {
            if (f.getId() == null) {
                pm.makePersistent(new Actividad(f.getNombre(), f.getDescripcion(), f.getDia(),
                        f.getHora(), f.getCupo(), f.isActiva()));
            } else {
                obtener(pm, f.getId()).actualizar(f.getNombre(), f.getDescripcion(), f.getDia(),
                        f.getHora(), f.getCupo(), f.isActiva());
            }
            return null;
        });
    }

    public void eliminar(Long id) {
        jdo.escribir(pm -> {
            pm.deletePersistent(obtener(pm, id));
            return null;
        });
    }

    static Actividad obtener(PersistenceManager pm, Long id) {
        if (id == null) throw new ReglaNegocioException("Debe seleccionarse una actividad.");
        try {
            return pm.getObjectById(Actividad.class, id);
        } catch (JDOObjectNotFoundException e) {
            throw new ReglaNegocioException("No existe la actividad solicitada.");
        }
    }
}
