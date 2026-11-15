package ar.edu.gimnasio.servicio;

import ar.edu.gimnasio.dtos.ActividadVista;
import ar.edu.gimnasio.dtos.InscripcionVista;
import ar.edu.gimnasio.dtos.SocioVista;
import ar.edu.gimnasio.excepcion.ReglaNegocioException;
import ar.edu.gimnasio.repositorio.ActividadRepositorio;
import ar.edu.gimnasio.repositorio.InscripcionRepositorio;
import ar.edu.gimnasio.repositorio.SocioRepositorio;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InscripcionServicioTest {
    @Mock InscripcionRepositorio inscripciones;
    @Mock SocioRepositorio socios;
    @Mock ActividadRepositorio actividades;
    InscripcionServicio servicio;

    @BeforeEach void preparar() {
        servicio = new InscripcionServicio(inscripciones, socios, actividades);
    }

    private void permitirInscripcion() {
        when(socios.buscar(1L)).thenReturn(new SocioVista(1L, "Ana", "Perez", "12345678",
                "ana@correo.com", "", true));
        when(actividades.buscar(2L)).thenReturn(new ActividadVista(2L, "Yoga", "",
                "Lunes", "18:00", 1, true));
    }

    @Test void registraCuandoHayCupo() {
        permitirInscripcion();
        when(inscripciones.todas()).thenReturn(List.of());
        assertDoesNotThrow(() -> servicio.registrar(1L, 2L));
        verify(inscripciones).registrar(1L, 2L);
    }

    @Test void rechazaInscripcionDuplicada() {
        permitirInscripcion();
        when(inscripciones.todas()).thenReturn(List.of(new InscripcionVista(1L, 1L,
                "Perez, Ana", 2L, "Yoga", LocalDate.now(), "ACTIVA")));
        ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
                () -> servicio.registrar(1L, 2L));
        assertTrue(error.getMessage().contains("ya esta inscripto"));
        verify(inscripciones, never()).registrar(anyLong(), anyLong());
    }

    @Test void rechazaActividadSinCupo() {
        permitirInscripcion();
        when(inscripciones.todas()).thenReturn(List.of(new InscripcionVista(1L, 5L,
                "Gomez, Luis", 2L, "Yoga", LocalDate.now(), "ACTIVA")));
        ReglaNegocioException error = assertThrows(ReglaNegocioException.class,
                () -> servicio.registrar(1L, 2L));
        assertTrue(error.getMessage().contains("cupos"));
        verify(inscripciones, never()).registrar(anyLong(), anyLong());
    }

    @Test void permiteNuevoCupoSiLaInscripcionAnteriorFueCancelada() {
        permitirInscripcion();
        when(inscripciones.todas()).thenReturn(List.of(new InscripcionVista(1L, 1L,
                "Perez, Ana", 2L, "Yoga", LocalDate.now(), "CANCELADA")));
        servicio.registrar(1L, 2L);
        verify(inscripciones).registrar(1L, 2L);
    }

    @Test void rechazaSocioInactivo() {
        when(socios.buscar(1L)).thenReturn(new SocioVista(1L, "Ana", "Perez", "12345678",
                "ana@correo.com", "", false));
        when(actividades.buscar(2L)).thenReturn(new ActividadVista(2L, "Yoga", "",
                "Lunes", "18:00", 1, true));
        assertThrows(ReglaNegocioException.class, () -> servicio.registrar(1L, 2L));
        verify(inscripciones, never()).registrar(anyLong(), anyLong());
    }
}
