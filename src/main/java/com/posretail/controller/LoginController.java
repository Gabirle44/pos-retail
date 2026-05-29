package com.posretail.controller;

import com.posretail.service.RecuperacionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private final RecuperacionService recuperacionService;

    public LoginController(RecuperacionService recuperacionService) {
        this.recuperacionService = recuperacionService;
    }

    @GetMapping("/login")
    public String login() { return "auth/login"; }

    @GetMapping("/")
    public String root() { return "redirect:/dashboard"; }

    @GetMapping("/recuperar")
    public String solicitarForm() { return "auth/solicitar-recuperacion"; }

    @PostMapping("/recuperar")
    public String solicitar(@RequestParam String email, Model model) {
        recuperacionService.solicitarRecuperacion(email);
        model.addAttribute("ok", "Si el correo existe se envió un enlace de recuperación.");
        return "auth/solicitar-recuperacion";
    }

    @GetMapping("/recuperar/cambiar")
    public String cambiarForm(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "auth/cambiar-password";
    }

    @PostMapping("/recuperar/cambiar")
    public String cambiar(@RequestParam String token,
                          @RequestParam String password,
                          Model model) {
        String err = recuperacionService.cambiarPassword(token, password);
        if (err != null) {
            model.addAttribute("error", err);
            model.addAttribute("token", token);
            return "auth/cambiar-password";
        }
        return "redirect:/login?reset";
    }
}
