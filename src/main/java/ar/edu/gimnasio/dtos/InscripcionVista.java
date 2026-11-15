package ar.edu.gimnasio.dtos;

import ar.edu.gimnasio.dominio.Inscripcion;
import java.time.LocalDate;

public record InscripcionVista(Long id, Long socioId, String socio,
                              Long actividadId, String actividad,
                              LocalDate fecha, String estado) {
    public static InscripcionVista desde(Inscripcion i) {
        return new InscripcionVista(
                i.getId(), i.getSocio().getId(),
                i.getSocio().getApellido() + ", " + i.getSocio().getNombre(),
                i.getActividad().getId(), i.getActividad().getNombre(),
                i.getFecha(), i.getEstado());
    }
    public boolean estaActiva() { return "ACTIVA".equals(estado); }
}
