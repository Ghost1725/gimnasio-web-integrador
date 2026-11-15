package ar.edu.gimnasio.controlador;

import ar.edu.gimnasio.dtos.ActividadFormulario;
import ar.edu.gimnasio.excepcion.ReglaNegocioException;
import ar.edu.gimnasio.servicio.ActividadServicio;
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
@RequestMapping("/actividades")
public class ActividadesControlador {
    private final ActividadServicio servicio;

    public ActividadesControlador(ActividadServicio servicio) { this.servicio = servicio; }

    @GetMapping
    public String listado(@RequestParam(defaultValue = "") String buscar, Model model) {
        model.addAttribute("actividades", servicio.listar(buscar));
        model.addAttribute("buscar", buscar);
        return "actividades/lista";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("form", new ActividadFormulario());
        model.addAttribute("titulo", "Nueva actividad");
        return "actividades/formulario";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("form", ActividadFormulario.desde(servicio.buscar(id)));
        model.addAttribute("titulo", "Editar actividad");
        return "actividades/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("form") ActividadFormulario form,
                          BindingResult errores, Model model, RedirectAttributes flash) {
        if (errores.hasErrors()) {
            model.addAttribute("titulo", form.getId() == null ? "Nueva actividad" : "Editar actividad");
            return "actividades/formulario";
        }
        try {
            servicio.guardar(form);
            flash.addFlashAttribute("exito", "Actividad guardada correctamente.");
            return "redirect:/actividades";
        } catch (ReglaNegocioException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("titulo", form.getId() == null ? "Nueva actividad" : "Editar actividad");
            return "actividades/formulario";
        }
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        try {
            servicio.eliminar(id);
            flash.addFlashAttribute("exito", "Actividad eliminada correctamente.");
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/actividades";
    }
}
