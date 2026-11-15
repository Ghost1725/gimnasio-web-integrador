package ar.edu.gimnasio.servicio;

import ar.edu.gimnasio.dtos.SocioFormulario;
import ar.edu.gimnasio.dtos.SocioVista;
import ar.edu.gimnasio.excepcion.ReglaNegocioException;
import ar.edu.gimnasio.repositorio.InscripcionRepositorio;
import ar.edu.gimnasio.repositorio.SocioRepositorio;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;

/** Reglas de negocio de los socios: DNI unico y bajas sin inscripciones. */
@Service
public class SocioServicio {
    private final SocioRepositorio socios;
    private final InscripcionRepositorio inscripciones;

    public SocioServicio(SocioRepositorio socios, InscripcionRepositorio inscripciones) {
        this.socios = socios;
        this.inscripciones = inscripciones;
    }

    public List<SocioVista> listar(String filtro) {
        String q = normalizar(filtro).toLowerCase(Locale.ROOT);
        return socios.todos().stream()
                .filter(s -> q.isBlank()
                        || s.nombre().toLowerCase(Locale.ROOT).contains(q)
                        || s.apellido().toLowerCase(Locale.ROOT).contains(q)
                        || s.dni().contains(q))
                .toList();
    }

    public SocioVista buscar(Long id) { return socios.buscar(id); }

    public void guardar(SocioFormulario f) {
        f.setNombre(normalizar(f.getNombre()));
        f.setApellido(normalizar(f.getApellido()));
        f.setDni(normalizar(f.getDni()));
        f.setEmail(normalizar(f.getEmail()));
        f.setTelefono(normalizar(f.getTelefono()));
        if (f.getNombre().isBlank() || f.getApellido().isBlank() || f.getEmail().isBlank()) {
            throw new ReglaNegocioException("Nombre, apellido y correo son obligatorios.");
        }
        if (!f.getDni().matches("[0-9]{7,10}")) {
            throw new ReglaNegocioException("El DNI debe contener de 7 a 10 digitos numericos.");
        }
        boolean repetido = socios.todos().stream()
                .anyMatch(s -> s.dni().equals(f.getDni()) && !s.id().equals(f.getId()));
        if (repetido) throw new ReglaNegocioException("Ya existe un socio con ese DNI.");
        socios.guardar(f);
    }

    public void eliminar(Long id) {
        // Incluye inscripciones canceladas: se preserva el historial.
        boolean relacionado = inscripciones.todas().stream().anyMatch(i -> i.socioId().equals(id));
        if (relacionado) {
            throw new ReglaNegocioException("No se puede eliminar: el socio tiene inscripciones. "
                    + "Podes marcarlo como inactivo.");
        }
        socios.eliminar(id);
    }

    private static String normalizar(String valor) { return valor == null ? "" : valor.trim(); }
}
