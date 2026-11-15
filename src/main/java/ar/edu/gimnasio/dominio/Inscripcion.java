package ar.edu.gimnasio.dominio;

import java.time.LocalDate;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Column;

/** Entidad intermedia que conserva historial de altas y cancelaciones. */
@PersistenceCapable(identityType = IdentityType.APPLICATION, table = "INSCRIPCION")
public class Inscripcion {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent(defaultFetchGroup = "true")
    @Column(name = "SOCIO_ID")
    private Socio socio;

    @Persistent(defaultFetchGroup = "true")
    @Column(name = "ACTIVIDAD_ID")
    private Actividad actividad;

    private LocalDate fecha;

    @Column(length = 15)
    private String estado;

    protected Inscripcion() { }

    public Inscripcion(Socio socio, Actividad actividad, LocalDate fecha) {
        this.socio = socio;
        this.actividad = actividad;
        this.fecha = fecha;
        this.estado = "ACTIVA";
    }

    public void cancelar() { this.estado = "CANCELADA"; }

    public Long getId() { return id; }
    public Socio getSocio() { return socio; }
    public Actividad getActividad() { return actividad; }
    public LocalDate getFecha() { return fecha; }
    public String getEstado() { return estado; }
    public boolean estaActiva() { return "ACTIVA".equals(estado); }
}
