package com.devstormlife.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
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
            // 1. Ativa o suporte ao CORS configurado na sua aplicação
            .cors(Customizer.withDefaults())
            
            // 2. Desabilita o CSRF (necessário para APIs REST com POST/PUT)
            .csrf(csrf -> csrf.disable())

            // 3. Define a gestão de sessão como STATELESS (API REST não guarda estado nem grava cookies de sessão)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 4. Configuração das permissões de rotas
            .authorizeHttpRequests(auth -> auth
                // Libera requisições PREFLIGHT do CORS (OPTIONS)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                // Libera todas as rotas públicas da aplicação
                .requestMatchers("/usuarios/**", "/api/usuarios/**").permitAll()
                .requestMatchers("/funcionarios/**", "/api/funcionarios/**").permitAll()
                .requestMatchers("/subestacoes/**", "/api/subestacoes/**").permitAll()
                
                // Exige autenticação para qualquer outra rota não listada acima
                .anyRequest().authenticated()
            );

        return http.build();
    }
}