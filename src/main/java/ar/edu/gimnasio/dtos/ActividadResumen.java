package ar.edu.gimnasio.dtos;

public record ActividadResumen(ActividadVista actividad, long ocupados) {
    public long disponibles() { return Math.max(0, actividad.cupo() - ocupados); }
}
