package com.posretail.config;

import com.posretail.model.Rol;
import com.posretail.model.Usuario;
import com.posretail.repository.RolRepository;
import com.posretail.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Garantiza que el usuario admin exista con la contraseña 'Admin123!' encriptada en BCrypt.
 * Si ya existe, no lo modifica.
 */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initAdmin(UsuarioRepository usuarioRepository,
                                RolRepository rolRepository,
                                PasswordEncoder passwordEncoder) {
        return args -> {
            if (usuarioRepository.findByUsername("admin").isEmpty()) {
                Rol admin = rolRepository.findAll().stream()
                        .filter(r -> "ADMIN".equals(r.getNombre())).findFirst()
                        .orElseGet(() -> {
                            Rol r = new Rol();
                            r.setNombre("ADMIN");
                            r.setDescripcion("Administrador");
                            return rolRepository.save(r);
                        });

                Usuario u = new Usuario();
                u.setRol(admin);
                u.setUsername("admin");
                u.setEmail("admin@posretail.com");
                u.setPasswordHash(passwordEncoder.encode("Admin123!"));
                usuarioRepository.save(u);
                System.out.println(">> Usuario admin creado. user=admin / pass=Admin123!");
            } else {
    // Asegurar que la contraseña sea válida con el encoder actual
    Usuario u = usuarioRepository.findByUsername("admin").get();
    if (!u.getPasswordHash().startsWith("$2")) {
        u.setPasswordHash(passwordEncoder.encode("Admin123!"));
        usuarioRepository.save(u);
        System.out.println(">> Password de admin actualizada a BCrypt.");
    }
}
        };
    }
}
