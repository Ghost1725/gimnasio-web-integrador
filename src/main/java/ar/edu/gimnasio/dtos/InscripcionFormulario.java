package ar.edu.gimnasio.dtos;

import jakarta.validation.constraints.NotNull;

public class InscripcionFormulario {
    @NotNull(message = "Elegi un socio")
    private Long socioId;
    @NotNull(message = "Elegi una actividad")
    private Long actividadId;

    public Long getSocioId() { return socioId; }
    public void setSocioId(Long socioId) { this.socioId = socioId; }
    public Long getActividadId() { return actividadId; }
    public void setActividadId(Long actividadId) { this.actividadId = actividadId; }
}
