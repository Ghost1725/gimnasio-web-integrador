package ar.edu.gimnasio.dtos;

import ar.edu.gimnasio.dominio.Actividad;

public record ActividadVista(Long id, String nombre, String descripcion,
                            String dia, String hora, int cupo, boolean activa) {
    public static ActividadVista desde(Actividad a) {
        return new ActividadVista(a.getId(), a.getNombre(), a.getDescripcion(),
                a.getDia(), a.getHora(), a.getCupo(), a.isActiva());
    }
}
