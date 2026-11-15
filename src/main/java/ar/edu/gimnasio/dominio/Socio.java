package ar.edu.gimnasio.dominio;

import java.util.HashSet;
import java.util.Set;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

/** Un socio puede participar en muchas inscripciones (1 a N). */
@PersistenceCapable(identityType = IdentityType.APPLICATION, table = "SOCIO")
public class Socio {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Column(length = 70)
    private String nombre;
    @Column(length = 70)
    private String apellido;
    @Column(length = 20)
    private String dni;
    @Column(length = 120)
    private String email;
    @Column(length = 30)
    private String telefono;
    private boolean activo;

    @Persistent(mappedBy = "socio")
    private Set<Inscripcion> inscripciones = new HashSet<>();

    protected Socio() { }

    public Socio(String nombre, String apellido, String dni, String email, String telefono, boolean activo) {
        actualizar(nombre, apellido, dni, email, telefono, activo);
    }

    public void actualizar(String nombre, String apellido, String dni, String email, String telefono, boolean activo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.telefono = telefono;
        this.activo = activo;
    }

    public void agregarInscripcion(Inscripcion inscripcion) {
        inscripciones.add(inscripcion);
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getDni() { return dni; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public boolean isActivo() { return activo; }
}
