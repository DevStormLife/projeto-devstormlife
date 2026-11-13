package com.devstormlife.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.devstormlife.api.entities.Usuario;
import com.devstormlife.api.repositories.UsuarioRepository;

@Component
public class BootstrapAdmin implements CommandLineRunner {
    private final UsuarioRepository users;
    private final BCryptPasswordEncoder passwords;
    @Value("${stormlife.bootstrap.email:}")
    private String email;
    @Value("${stormlife.bootstrap.password:}")
    private String password;

    public BootstrapAdmin(UsuarioRepository users, BCryptPasswordEncoder passwords) {
        this.users = users;
        this.passwords = passwords;
    }

    @Override
    public void run(String... args) {
        if (email.isBlank() || password.isBlank()) return;
        if (users.findByEmail(email).isPresent()) return;
        Usuario admin = new Usuario(email, passwords.encode(password), "CHEFE");
        users.save(admin);
    }
}
