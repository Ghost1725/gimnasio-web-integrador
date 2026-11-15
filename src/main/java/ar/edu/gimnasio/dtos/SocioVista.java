package ar.edu.gimnasio.dtos;

import ar.edu.gimnasio.dominio.Socio;

public record SocioVista(Long id, String nombre, String apellido, String dni,
                        String email, String telefono, boolean activo) {
    public static SocioVista desde(Socio s) {
        return new SocioVista(s.getId(), s.getNombre(), s.getApellido(),
                s.getDni(), s.getEmail(), s.getTelefono(), s.isActivo());
    }
    public String nombreCompleto() { return apellido + ", " + nombre; }
}
