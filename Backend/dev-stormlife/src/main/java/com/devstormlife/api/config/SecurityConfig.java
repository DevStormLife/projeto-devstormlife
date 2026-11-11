package com.devstormlife.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
            
            // 2. Desabilita o CSRF (essencial para requisições POST via API REST)
            .csrf(csrf -> csrf.disable())
            
            // 3. Libera as rotas públicas (Login, Cadastros, etc.)
            .authorizeHttpRequests(auth -> auth
                // Libera requisições de checagem CORS
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                // Libera rotas de Usuários (com e sem /api)
                .requestMatchers("/api/usuarios/**", "/usuarios/**").permitAll()
                
                // Libera rotas de Funcionários (incluindo /funcionarios/cadastro)
                .requestMatchers("/api/funcionarios/**", "/funcionarios/**").permitAll()
                
                // Libera rotas de Subestações
                .requestMatchers("/api/subestacoes/**", "/subestacoes/**").permitAll()
                
                .anyRequest().authenticated()
            );
        
        return http.build();
    }
}