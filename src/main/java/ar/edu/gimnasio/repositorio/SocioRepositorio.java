package ar.edu.gimnasio.repositorio;

import ar.edu.gimnasio.config.UnidadDeTrabajoJdo;
import ar.edu.gimnasio.dominio.Socio;
import ar.edu.gimnasio.dtos.SocioFormulario;
import ar.edu.gimnasio.dtos.SocioVista;
import ar.edu.gimnasio.excepcion.ReglaNegocioException;
import java.util.Comparator;
import java.util.List;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import org.springframework.stereotype.Repository;

@Repository
public class SocioRepositorio {
    private final UnidadDeTrabajoJdo jdo;

    public SocioRepositorio(UnidadDeTrabajoJdo jdo) { this.jdo = jdo; }

    public List<SocioVista> todos() {
        return jdo.leer(pm -> ConsultasJdo.todos(pm, Socio.class).stream()
                .map(SocioVista::desde)
                .sorted(Comparator.comparing(SocioVista::apellido, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(SocioVista::nombre, String.CASE_INSENSITIVE_ORDER))
                .toList());
    }

    public SocioVista buscar(Long id) {
        return jdo.leer(pm -> SocioVista.desde(obtener(pm, id)));
    }

    public void guardar(SocioFormulario f) {
        jdo.escribir(pm -> {
            if (f.getId() == null) {
                pm.makePersistent(new Socio(f.getNombre(), f.getApellido(), f.getDni(),
                        f.getEmail(), f.getTelefono(), f.isActivo()));
            } else {
                obtener(pm, f.getId()).actualizar(f.getNombre(), f.getApellido(), f.getDni(),
                        f.getEmail(), f.getTelefono(), f.isActivo());
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

    static Socio obtener(PersistenceManager pm, Long id) {
        if (id == null) throw new ReglaNegocioException("Debe seleccionarse un socio.");
        try {
            return pm.getObjectById(Socio.class, id);
        } catch (JDOObjectNotFoundException e) {
            throw new ReglaNegocioException("No existe el socio solicitado.");
        }
    }
}
