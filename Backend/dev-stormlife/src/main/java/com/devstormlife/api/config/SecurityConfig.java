package com.devstormlife.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        http
            // 1. Ativa o CORS integrado para conversar com a sua classe CorsConfig
            .cors(Customizer.withDefaults()) 
            
            // 2. Desabilita o CSRF (essencial para o POST funcionar)
            .csrf(csrf -> csrf.disable())
            
            // 3. Libera as rotas e suas sub-rotas
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/usuarios", "/usuarios/**").permitAll()
                .requestMatchers("/funcionarios", "/funcionarios/**").permitAll()
                .requestMatchers("/subestacoes", "/subestacoes/**").permitAll() // Rota da subestação liberada!
                .anyRequest().authenticated()
            );
        
        return http.build();
    }
}