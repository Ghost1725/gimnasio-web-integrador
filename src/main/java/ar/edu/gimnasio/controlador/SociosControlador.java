package ar.edu.gimnasio.controlador;

import ar.edu.gimnasio.dtos.SocioFormulario;
import ar.edu.gimnasio.excepcion.ReglaNegocioException;
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
@RequestMapping("/socios")
public class SociosControlador {
    private final SocioServicio servicio;

    public SociosControlador(SocioServicio servicio) { this.servicio = servicio; }

    @GetMapping
    public String listado(@RequestParam(defaultValue = "") String buscar, Model model) {
        model.addAttribute("socios", servicio.listar(buscar));
        model.addAttribute("buscar", buscar);
        return "socios/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("form", new SocioFormulario());
        model.addAttribute("titulo", "Nuevo socio");
        return "socios/formulario";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("form", SocioFormulario.desde(servicio.buscar(id)));
        model.addAttribute("titulo", "Editar socio");
        return "socios/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("form") SocioFormulario form,
                          BindingResult errores, Model model, RedirectAttributes flash) {
        if (errores.hasErrors()) {
            model.addAttribute("titulo", form.getId() == null ? "Nuevo socio" : "Editar socio");
            return "socios/formulario";
        }
        try {
            servicio.guardar(form);
            flash.addFlashAttribute("exito", "Socio guardado correctamente.");
            return "redirect:/socios";
        } catch (ReglaNegocioException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("titulo", form.getId() == null ? "Nuevo socio" : "Editar socio");
            return "socios/formulario";
        }
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        try {
            servicio.eliminar(id);
            flash.addFlashAttribute("exito", "Socio eliminado correctamente.");
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/socios";
    }
}
