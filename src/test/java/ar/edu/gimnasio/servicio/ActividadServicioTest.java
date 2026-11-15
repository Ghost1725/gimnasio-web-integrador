package ar.edu.gimnasio.servicio;

import ar.edu.gimnasio.dtos.ActividadFormulario;
import ar.edu.gimnasio.dtos.InscripcionVista;
import ar.edu.gimnasio.excepcion.ReglaNegocioException;
import ar.edu.gimnasio.repositorio.ActividadRepositorio;
import ar.edu.gimnasio.repositorio.InscripcionRepositorio;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ActividadServicioTest {
    @Test void impideReducirCupoDebajoDeInscriptos() {
        ActividadRepositorio actividades = mock(ActividadRepositorio.class);
        InscripcionRepositorio inscripciones = mock(InscripcionRepositorio.class);
        ActividadServicio servicio = new ActividadServicio(actividades, inscripciones);
        ActividadFormulario f = new ActividadFormulario();
        f.setId(2L); f.setNombre("Spinning"); f.setDia("Martes");
        f.setHora("19:00"); f.setCupo(1);
        when(inscripciones.todas()).thenReturn(List.of(
                new InscripcionVista(1L, 1L, "Socio 1", 2L, "Spinning", LocalDate.now(), "ACTIVA"),
                new InscripcionVista(2L, 2L, "Socio 2", 2L, "Spinning", LocalDate.now(), "ACTIVA")));
        assertThrows(ReglaNegocioException.class, () -> servicio.guardar(f));
        verify(actividades, never()).guardar(any());
    }
}
