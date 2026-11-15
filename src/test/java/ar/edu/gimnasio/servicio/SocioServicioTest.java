package ar.edu.gimnasio.servicio;

import ar.edu.gimnasio.dtos.InscripcionVista;
import ar.edu.gimnasio.dtos.SocioFormulario;
import ar.edu.gimnasio.dtos.SocioVista;
import ar.edu.gimnasio.excepcion.ReglaNegocioException;
import ar.edu.gimnasio.repositorio.InscripcionRepositorio;
import ar.edu.gimnasio.repositorio.SocioRepositorio;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SocioServicioTest {
    @Mock SocioRepositorio socios;
    @Mock InscripcionRepositorio inscripciones;
    SocioServicio servicio;

    @BeforeEach void preparar() { servicio = new SocioServicio(socios, inscripciones); }

    @Test void rechazaDniDuplicado() {
        SocioFormulario f = new SocioFormulario();
        f.setNombre("Carlos"); f.setApellido("Diaz"); f.setDni("12345678");
        f.setEmail("c@correo.com");
        when(socios.todos()).thenReturn(List.of(new SocioVista(3L, "Ana", "Perez",
                "12345678", "a@correo.com", "", true)));
        assertThrows(ReglaNegocioException.class, () -> servicio.guardar(f));
        verify(socios, never()).guardar(any());
    }

    @Test void noEliminaSocioConHistorial() {
        when(inscripciones.todas()).thenReturn(List.of(new InscripcionVista(4L, 1L,
                "Perez, Ana", 3L, "Yoga", LocalDate.now(), "CANCELADA")));
        assertThrows(ReglaNegocioException.class, () -> servicio.eliminar(1L));
        verify(socios, never()).eliminar(anyLong());
    }
}
