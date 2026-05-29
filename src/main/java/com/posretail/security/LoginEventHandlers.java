package com.posretail.security;

import com.posretail.model.IntentoLogin;
import com.posretail.model.Usuario;
import com.posretail.repository.IntentoLoginRepository;
import com.posretail.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginEventHandlers {

    public static final int MAX_INTENTOS = 3;

    private final UsuarioRepository usuarioRepository;
    private final IntentoLoginRepository intentoLoginRepository;

    public LoginEventHandlers(UsuarioRepository usuarioRepository,
                              IntentoLoginRepository intentoLoginRepository) {
        this.usuarioRepository = usuarioRepository;
        this.intentoLoginRepository = intentoLoginRepository;
    }

    public SuccessHandler successHandler() { return new SuccessHandler(); }
    public FailureHandler failureHandler() { return new FailureHandler(); }

    public class SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
        public SuccessHandler() {
            setDefaultTargetUrl("/dashboard");
        }

        @Override
        public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res,
                                            Authentication auth) throws IOException, jakarta.servlet.ServletException {
            String username = auth.getName();
            usuarioRepository.findByUsername(username).ifPresent(u -> {
                u.setIntentosFallidos(0);
                usuarioRepository.save(u);

                IntentoLogin log = new IntentoLogin();
                log.setUsuario(u);
                log.setUsername(username);
                log.setExitoso(true);
                log.setIp(req.getRemoteAddr());
                intentoLoginRepository.save(log);
            });
            super.onAuthenticationSuccess(req, res, auth);
        }
    }

    public class FailureHandler extends SimpleUrlAuthenticationFailureHandler {

        @Override
        public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res,
                                            AuthenticationException ex) throws IOException, jakarta.servlet.ServletException {
            String username = req.getParameter("username");
            String mensaje = "Credenciales incorrectas";

            if (username != null) {
                Usuario u = usuarioRepository.findByUsername(username).orElse(null);
                if (u != null) {
                    if (Boolean.TRUE.equals(u.getBloqueado())) {
                        mensaje = "Usuario bloqueado. Use 'Olvidé mi contraseña' para recuperarlo.";
                    } else {
                        u.setIntentosFallidos(u.getIntentosFallidos() + 1);
                        if (u.getIntentosFallidos() >= MAX_INTENTOS) {
                            u.setBloqueado(true);
                            mensaje = "Usuario bloqueado tras " + MAX_INTENTOS + " intentos fallidos.";
                        } else {
                            int rest = MAX_INTENTOS - u.getIntentosFallidos();
                            mensaje = "Credenciales incorrectas. Intentos restantes: " + rest;
                        }
                        usuarioRepository.save(u);
                    }

                    IntentoLogin log = new IntentoLogin();
                    log.setUsuario(u);
                    log.setUsername(username);
                    log.setExitoso(false);
                    log.setIp(req.getRemoteAddr());
                    intentoLoginRepository.save(log);
                }
            }

            setDefaultFailureUrl("/login?error=" + java.net.URLEncoder.encode(mensaje, "UTF-8"));
            super.onAuthenticationFailure(req, res, ex);
        }
    }
}
