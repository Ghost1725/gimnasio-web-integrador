package ar.edu.gimnasio.controlador;

import ar.edu.gimnasio.dtos.InscripcionFormulario;
import ar.edu.gimnasio.excepcion.ReglaNegocioException;
import ar.edu.gimnasio.servicio.ActividadServicio;
import ar.edu.gimnasio.servicio.InscripcionServicio;
import ar.edu.gimnasio.servicio.SocioServicio;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/inscripciones")
public class InscripcionesControlador {
    private final InscripcionServicio inscripciones;
    private final SocioServicio socios;
    private final ActividadServicio actividades;

    public InscripcionesControlador(InscripcionServicio inscripciones,
                                   SocioServicio socios, ActividadServicio actividades) {
        this.inscripciones = inscripciones;
        this.socios = socios;
        this.actividades = actividades;
    }

    @GetMapping
    public String listado(@RequestParam(required = false) Long actividadId, Model model) {
        model.addAttribute("inscripciones", inscripciones.listar(actividadId));
        model.addAttribute("actividades", actividades.listar(""));
        model.addAttribute("actividadId", actividadId);
        return "inscripciones/lista";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("form", new InscripcionFormulario());
        datosFormulario(model);
        return "inscripciones/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("form") InscripcionFormulario form,
                          BindingResult errores, Model model, RedirectAttributes flash) {
        if (errores.hasErrors()) {
            datosFormulario(model);
            return "inscripciones/formulario";
        }
        try {
            inscripciones.registrar(form.getSocioId(), form.getActividadId());
            flash.addFlashAttribute("exito", "Inscripcion realizada correctamente.");
            return "redirect:/inscripciones";
        } catch (ReglaNegocioException e) {
            model.addAttribute("error", e.getMessage());
            datosFormulario(model);
            return "inscripciones/formulario";
        }
    }

    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable Long id, RedirectAttributes flash) {
        try {
            inscripciones.cancelar(id);
            flash.addFlashAttribute("exito", "Inscripcion cancelada. Se libero un cupo.");
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/inscripciones";
    }

    private void datosFormulario(Model model) {
        model.addAttribute("socios", socios.listar("").stream().filter(s -> s.activo()).toList());
        model.addAttribute("actividades", actividades.listar("").stream()
                .filter(a -> a.actividad().activa()).toList());
    }
}
