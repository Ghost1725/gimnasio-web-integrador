package ar.edu.gimnasio.dominio;

import java.util.HashSet;
import java.util.Set;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

/** Una actividad tiene muchas inscripciones (1 a N). */
@PersistenceCapable(identityType = IdentityType.APPLICATION, table = "ACTIVIDAD")
public class Actividad {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Column(length = 90)
    private String nombre;
    @Column(length = 250)
    private String descripcion;
    @Column(length = 20)
    private String dia;
    @Column(length = 5)
    private String hora;
    private int cupo;
    private boolean activa;

    @Persistent(mappedBy = "actividad")
    private Set<Inscripcion> inscripciones = new HashSet<>();

    protected Actividad() { }

    public Actividad(String nombre, String descripcion, String dia, String hora, int cupo, boolean activa) {
        actualizar(nombre, descripcion, dia, hora, cupo, activa);
    }

    public void actualizar(String nombre, String descripcion, String dia, String hora, int cupo, boolean activa) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.dia = dia;
        this.hora = hora;
        this.cupo = cupo;
        this.activa = activa;
    }

    public void agregarInscripcion(Inscripcion inscripcion) {
        inscripciones.add(inscripcion);
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getDia() { return dia; }
    public String getHora() { return hora; }
    public int getCupo() { return cupo; }
    public boolean isActiva() { return activa; }
}
