package ar.edu.gimnasio.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SocioFormulario {
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 70, message = "Maximo 70 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 70, message = "Maximo 70 caracteres")
    private String apellido;

    @NotBlank(message = "El DNI es obligatorio")
    @Size(min = 7, max = 10, message = "El DNI debe tener entre 7 y 10 digitos")
    private String dni;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Escribi un correo valido")
    @Size(max = 120, message = "Maximo 120 caracteres")
    private String email;

    @Size(max = 30, message = "Maximo 30 caracteres")
    private String telefono;
    private boolean activo = true;

    public static SocioFormulario desde(SocioVista s) {
        SocioFormulario f = new SocioFormulario();
        f.id = s.id(); f.nombre = s.nombre(); f.apellido = s.apellido();
        f.dni = s.dni(); f.email = s.email(); f.telefono = s.telefono(); f.activo = s.activo();
        return f;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
