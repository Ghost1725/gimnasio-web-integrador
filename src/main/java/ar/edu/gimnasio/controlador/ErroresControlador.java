package ar.edu.gimnasio.controlador;

import ar.edu.gimnasio.excepcion.ReglaNegocioException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErroresControlador {
    @ExceptionHandler(ReglaNegocioException.class)
    public String regla(ReglaNegocioException e, Model model) {
        model.addAttribute("mensaje", e.getMessage());
        return "error-negocio";
    }
}
