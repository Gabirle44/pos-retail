package com.posretail.service;

import com.posretail.model.Usuario;
import com.posretail.repository.UsuarioRepository;
import com.posretail.security.PasswordValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RecuperacionService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Value("${app.base-url}")
    private String baseUrl;

    public RecuperacionService(UsuarioRepository usuarioRepository,
                               PasswordEncoder passwordEncoder,
                               JavaMailSender mailSender) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    /** Genera token y envía correo con enlace de recuperación. */
    @Transactional
    public void solicitarRecuperacion(String email) {
        Usuario u = usuarioRepository.findByEmail(email).orElse(null);
        if (u == null) return; // por seguridad no revelamos si existe
        String token = UUID.randomUUID().toString();
        u.setTokenRecuperacion(token);
        u.setTokenExpira(LocalDateTime.now().plusHours(1));
        usuarioRepository.save(u);

        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(email);
            msg.setSubject("POS Retail - Recuperación de contraseña");
            msg.setText("Hola " + u.getUsername() + ",\n\n" +
                    "Para restablecer tu contraseña haz clic en el siguiente enlace " +
                    "(válido por 1 hora):\n\n" +
                    baseUrl + "/recuperar/cambiar?token=" + token + "\n\n" +
                    "Si no solicitaste este cambio ignora este correo.");
            mailSender.send(msg);
        } catch (Exception e) {
            // En desarrollo el SMTP puede no estar configurado: imprimir enlace en consola
            System.out.println("[RECUPERACION] Enlace para " + email + " => " +
                    baseUrl + "/recuperar/cambiar?token=" + token);
        }
    }

    @Transactional
    public String cambiarPassword(String token, String nuevaPassword) {
        String err = PasswordValidator.validate(nuevaPassword);
        if (err != null) return err;

        Usuario u = usuarioRepository.findByTokenRecuperacion(token).orElse(null);
        if (u == null || u.getTokenExpira() == null || u.getTokenExpira().isBefore(LocalDateTime.now()))
            return "Token inválido o vencido";

        u.setPasswordHash(passwordEncoder.encode(nuevaPassword));
        u.setTokenRecuperacion(null);
        u.setTokenExpira(null);
        u.setIntentosFallidos(0);
        u.setBloqueado(false);
        usuarioRepository.save(u);
        return null;
    }
}
