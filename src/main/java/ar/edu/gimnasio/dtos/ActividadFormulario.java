package ar.edu.gimnasio.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ActividadFormulario {
    private Long id;
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 90, message = "Maximo 90 caracteres")
    private String nombre;
    @Size(max = 250, message = "Maximo 250 caracteres")
    private String descripcion;
    @NotBlank(message = "Elegi un dia")
    private String dia;
    @NotBlank(message = "La hora es obligatoria")
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "Usa el formato HH:mm")
    private String hora;
    @NotNull(message = "El cupo es obligatorio")
    @Min(value = 1, message = "El cupo minimo es 1")
    private Integer cupo;
    private boolean activa = true;

    public static ActividadFormulario desde(ActividadVista a) {
        ActividadFormulario f = new ActividadFormulario();
        f.id = a.id(); f.nombre = a.nombre(); f.descripcion = a.descripcion();
        f.dia = a.dia(); f.hora = a.hora(); f.cupo = a.cupo(); f.activa = a.activa();
        return f;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getDia() { return dia; }
    public void setDia(String dia) { this.dia = dia; }
    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }
    public Integer getCupo() { return cupo; }
    public void setCupo(Integer cupo) { this.cupo = cupo; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
}
