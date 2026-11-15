package ar.edu.gimnasio.controlador;

import ar.edu.gimnasio.servicio.ActividadServicio;
import ar.edu.gimnasio.servicio.InscripcionServicio;
import ar.edu.gimnasio.servicio.SocioServicio;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioControlador {
    private final SocioServicio socios;
    private final ActividadServicio actividades;
    private final InscripcionServicio inscripciones;

    public InicioControlador(SocioServicio socios, ActividadServicio actividades,
                            InscripcionServicio inscripciones) {
        this.socios = socios;
        this.actividades = actividades;
        this.inscripciones = inscripciones;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("totalSocios", socios.listar("").stream().filter(s -> s.activo()).count());
        model.addAttribute("totalActividades", actividades.listar("").stream()
                .filter(a -> a.actividad().activa()).count());
        model.addAttribute("totalInscripciones", inscripciones.listar(null).stream()
                .filter(i -> i.estaActiva()).count());
        model.addAttribute("actividades", actividades.listar("").stream().limit(5).toList());
        return "inicio";
    }
}
